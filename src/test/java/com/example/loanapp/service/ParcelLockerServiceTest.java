package com.example.loanapp.service;

import com.example.loanapp.model.ParcelLocker;
import com.example.loanapp.model.Size;
import com.example.loanapp.repo.ParcelLockerRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@SpringBootTest
class ParcelLockerServiceTest {
    @InjectMocks
    private ParcelLockerService parcelLockerService;
    @Mock
    private ParcelLockerRepo parcelLockerRepo;


    @Test
    void findFreeParcelLocker() {
        // given
        Size sizeParcel = Size.MEDIUM;
        ParcelLocker mediumParcelLocker = new ParcelLocker(2L,2,Size.MEDIUM,true,false);

        List<ParcelLocker> freeParcelLockers = Arrays.asList(
                new ParcelLocker(1L,1,Size.SMALL,true,false),
                mediumParcelLocker,
                new ParcelLocker(3L,3,Size.LARGE,true,false)
        );

        // Konfiguracja moka
        when(parcelLockerRepo.findAllFreeParcelLockers()).thenReturn(freeParcelLockers);

        // when
        ParcelLocker result = parcelLockerService.findFreeParcelLocker(sizeParcel);

        // then
        verify(parcelLockerRepo).findAllFreeParcelLockers();
        assertEquals(mediumParcelLocker, result);
    }

    @Test
    void updateStatusParcelLocker() {
    }
}