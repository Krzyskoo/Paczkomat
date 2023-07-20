package com.example.notatnik.service;

import com.example.notatnik.model.Paczka;
import com.example.notatnik.model.Paczkomat;
import com.example.notatnik.model.User;
import com.example.notatnik.repo.PaczkaRepo;
import com.example.notatnik.repo.PaczkomatRepo;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaczkaService {
    private final PaczkaRepo paczkaRepo;
    private final PaczkomatRepo paczkomatRepo;
    private final EmailService emailService;

    public PaczkaService(PaczkaRepo paczkaRepo, PaczkomatRepo paczkomatRepo, EmailService emailService) {
        this.paczkaRepo = paczkaRepo;
        this.paczkomatRepo = paczkomatRepo;
        this.emailService = emailService;
    }
    @Transactional
    @Scheduled(fixedDelay = 60000)
    public void aktualizujPaczki() {
        List<Paczka> paczki = paczkaRepo.findAll();
        List<Long> paczkiDoZwolnienei= new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now();
        paczki = paczki.stream()
                .peek(paczka -> {
                    if (currentTime.isAfter(paczka.getDataWaznosci())) {
                        // Usuń paczkę, jeśli currentTime jest po terminie
                        paczkiDoZwolnienei.add(paczka.getPaczkomat().getId());
                    }

                    if ((currentTime.isEqual(paczka.getCzasWaznosci()) || currentTime.isAfter(paczka.getCzasWaznosci())
                    && !paczka.isRememberMailSended())) {
                        try {
                            emailService.wyslijEmailPrzypomnienie(paczka);
                            paczka.setRememberMailSended(true);
                        } catch (MessagingException e) {
                            // Obsłuż wyjątek
                            throw new RuntimeException(e);
                        }
                    }
                })
                .collect(Collectors.toList());
            if (!paczkiDoZwolnienei.isEmpty()) {
                paczkomatRepo.updatePaczkomatStatus(paczkiDoZwolnienei);
            }

        // Zapisz zmiany w bazie danych
        paczkaRepo.saveAll(paczki);
    }

    public Paczka nadajPaczke(String rozmiar, boolean isHtmlContent, User user) throws MessagingException {
        Paczkomat wolnaPaczkomat = znajdzWolnaPrzegrodke(rozmiar);

        if (wolnaPaczkomat == null) {
            return null; // Brak dostępnych przegródek
        }

        Paczka paczka = new Paczka();
        paczka.setRozmiar(rozmiar);
        paczka.setStatus("nadana");
        paczka.setEmailOdbiorcy(user.getEmail());
        paczka.setDataNadania(LocalDateTime.now());
        paczka.setKodOdbioru(generujKodOdbioru());
        paczka.setPaczkomat(wolnaPaczkomat);
        paczka.setUser(user);
        paczka.setRememberMailSended(false);

        LocalDateTime czasOdbioru = LocalDateTime.now().plus(rozmiar.equals("mala") ? Duration.ofMinutes(5) : Duration.ofHours(3));
        paczka.setDataWaznosci(czasOdbioru);
        long polowaCzasuMinuty = Duration.between(paczka.getDataNadania(), czasOdbioru).toMinutes() / 2;
        paczka.setCzasWaznosci(paczka.getDataWaznosci().minusMinutes(polowaCzasuMinuty));

        paczkaRepo.save(paczka);

        wolnaPaczkomat.setStatus("zajęta");
        paczkomatRepo.save(wolnaPaczkomat);

        emailService.wyslijEmailNadania(paczka.getEmailOdbiorcy(), paczka.getKodOdbioru(),isHtmlContent);

        return paczka;
    }

    public boolean odbierzPaczke(String kodOdbioru) {
        Paczka paczka = paczkaRepo.findByKodOdbioru(kodOdbioru);

        if (paczka == null) {
            return false; // Nie znaleziono paczki o podanym kodzie odbioru
        }

        if (paczka.getStatus().equals("odebrana")) {
            return false; // Paczka została już odebrana
        }

        LocalDateTime currentTime = LocalDateTime.now();

        if (currentTime.isAfter(paczka.getDataWaznosci())) {
            return false; // Przekroczono czas ważności paczki
        }

        paczka.setStatus("odebrana");
        paczkaRepo.save(paczka);

        Paczkomat Paczkomat = paczka.getPaczkomat();
        Paczkomat.setStatus("wolna");
        paczkomatRepo.save(Paczkomat);

        return true; // Paczka została pomyślnie odebrana
    }
    private Paczkomat znajdzWolnaPrzegrodke(String rozmiarPaczki) {
        List<Paczkomat> wolnePrzegrodki = paczkomatRepo.findByStatus("wolna");

        String[] rozmiaryPaczek = {"mala", "srednia", "duza"};

        Optional<Paczkomat> znalezionaPrzegrodka = Arrays.stream(rozmiaryPaczek)
                .filter(rozmiar -> rozmiar.equals(rozmiarPaczki)) // Wybieramy tylko rozmiar, który pasuje do paczki
                .map(rozmiar -> wolnePrzegrodki.stream()
                        .filter(przegrodka -> przegrodka.getRozmiarPaczki().equals(rozmiar))
                        .findFirst()
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();

        // Jeżeli nie znaleziono przegrody o dokładnym rozmiarze, to szukamy najbliższej większej
        if (znalezionaPrzegrodka.isEmpty()) {
            int indeksRozmiaru = Arrays.asList(rozmiaryPaczek).indexOf(rozmiarPaczki);

            znalezionaPrzegrodka = wolnePrzegrodki.stream()
                    .dropWhile(przegrodka -> Arrays.asList(rozmiaryPaczek).indexOf(przegrodka.getRozmiarPaczki()) <= indeksRozmiaru)
                    .findFirst();
        }

        // Brak dostępnych przegródek dla paczki o podanym rozmiarze lub większych
        return znalezionaPrzegrodka.orElse(null);
    }


    public String generujKodOdbioru() {
        int dlugosc = 8;
        String dostepneZnaki = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        return new Random().ints(dlugosc, 0, dostepneZnaki.length())
                .mapToObj(dostepneZnaki::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }


}
