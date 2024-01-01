package com.example.loanapp.repo;

import com.example.loanapp.model.ParcelLocker;
import com.example.loanapp.model.Size;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Modifying;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest
class ParcelLockerRepoTest {

    @Autowired
    private ParcelLockerRepo parcelLockerRepo;

    @Test
    @Transactional
    void shouldReturnAllFreeParcelLockers() {
        //given
        ParcelLocker parcelLocker =new ParcelLocker(1L,1, Size.SMALL,true,false);
        ParcelLocker parcelLocker2 =new ParcelLocker(2L,2, Size.SMALL,false,false);
        parcelLockerRepo.save(parcelLocker);
        parcelLockerRepo.save(parcelLocker2);
        //when
        List<ParcelLocker> result = parcelLockerRepo.findAllFreeParcelLockers();
        //then
        assertThat(result).contains(parcelLocker);
        assertThat(result).doesNotContain(parcelLocker2);

    }

    @Test
    @Transactional
    @Modifying
    void shouldUpdateStatus() {
        //given
        ParcelLocker parcelLocker =new ParcelLocker(1L,1, Size.SMALL,true,false);
        ParcelLocker parcelLocker2 =new ParcelLocker(2L,2, Size.SMALL,false,false);

        parcelLockerRepo.save(parcelLocker);
        parcelLockerRepo.save(parcelLocker2);
        //when
        parcelLockerRepo.updateStatus(parcelLocker.getId(), false);
        parcelLockerRepo.updateStatus(parcelLocker2.getId(), true);
        //then
        ParcelLocker firstUpdatedParcelLocker = parcelLockerRepo.findById(parcelLocker.getId()).orElse(null);
        ParcelLocker secondUpdatedParcelLocker = parcelLockerRepo.findById(parcelLocker2.getId()).orElse(null);

        assertThat(firstUpdatedParcelLocker.isFree()).isFalse();
        assertThat(secondUpdatedParcelLocker.isFree()).isTrue();

    }
}