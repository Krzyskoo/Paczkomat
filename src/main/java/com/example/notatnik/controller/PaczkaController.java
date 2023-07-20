package com.example.notatnik.controller;

import com.example.notatnik.model.Paczka;
import com.example.notatnik.model.User;
import com.example.notatnik.service.PaczkaService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paczki")
public class PaczkaController {
    private final PaczkaService paczkaService;

    public PaczkaController(PaczkaService paczkaService) {
        this.paczkaService = paczkaService;
    }

    @PostMapping("/nadaj/{rozmiar}")
    public ResponseEntity<?> nadajPaczke(@PathVariable("rozmiar") String rozmiar,
                                         @AuthenticationPrincipal User user) throws MessagingException {

        Paczka paczka = paczkaService.nadajPaczke(rozmiar,true,user);

        if (paczka == null) {
            return ResponseEntity.badRequest().body("Brak dostępnych przegródek.");
        }

        return ResponseEntity.ok("Paczka została nadana.");
    }

    @GetMapping ("/odbierz/{kodOdbioru}")
    public ResponseEntity<?> odbierzPaczke(@PathVariable("kodOdbioru") String kodOdbioru) {
        boolean success = paczkaService.odbierzPaczke(kodOdbioru);

        if (!success) {
            return ResponseEntity.badRequest().body("Nie można odebrać paczki.");
        }

        return ResponseEntity.ok("Paczka została pomyślnie odebrana.");
    }
}
