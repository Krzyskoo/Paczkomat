package com.example.loanapp.service;

import com.example.loanapp.model.ParcelLocker;
import com.example.loanapp.model.Size;
import com.example.loanapp.repo.ParcelLockerRepo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ParcelLockerService {
    private ParcelLockerRepo parcelLockerRepo;

    public ParcelLockerService(ParcelLockerRepo parcelLockerRepo) {
        this.parcelLockerRepo = parcelLockerRepo;
    }
    public ParcelLocker findFreeParcelLocker(Size sizeParcel){
        List<ParcelLocker> freeParcelsLocker = parcelLockerRepo.findAllFreeParcelLockers();
        Size [] sizeOfParcels = {Size.SMALL,Size.MEDIUM,Size.LARGE};

        Optional<ParcelLocker> findParcelLocker = Arrays.stream(sizeOfParcels)
                .filter(size -> size.equals(sizeParcel)) // Wybieramy tylko rozmiar, który pasuje do paczki
                .map(size -> freeParcelsLocker.stream()
                        .filter(parcelLocker -> parcelLocker.getSize().equals(size))
                        .findFirst()
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
        // Jeżeli nie znaleziono przegrody o dokładnym rozmiarze, to szukamy najbliższej większej
        if (findParcelLocker.isEmpty()) {
            int indeksRozmiaru = Arrays.asList(sizeOfParcels).indexOf(sizeParcel);

            findParcelLocker = freeParcelsLocker.stream()
                    .dropWhile(parcelLocker -> Arrays.asList(sizeParcel).indexOf(parcelLocker.getSize()) <= indeksRozmiaru)
                    .findFirst();
        }
        // Brak dostępnych przegródek dla paczki o podanym rozmiarze lub większych
        return findParcelLocker.orElse(null);

    }
    public void updateStatusParcelLocker(Long id, boolean newStatus) {
        parcelLockerRepo.updateStatus(id, newStatus);
    }


}
