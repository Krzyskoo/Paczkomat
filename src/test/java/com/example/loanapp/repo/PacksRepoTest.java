package com.example.loanapp.repo;

import com.example.loanapp.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PacksRepoTest {


    @Autowired
    private PacksRepo underTest;



    @Test
    void findByExpirationDateLessThan() {
       //given
        Packs packs= new Packs(1L,
                "kk@o2.pl",
                "bb@o2.pl",
                "123456",
                Size.MEDIUM,
                Status.TO_RECEIVE,
                false,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),new ParcelLocker(),new User());
        underTest.save(packs);

        //when
        List<Packs> result=underTest.findByExpirationDateLessThan(LocalDateTime.now().plusDays(3));

        //then
        assertThat(result).isEqualTo(packs);    }

    @Test
    void findByPickupCode() {
        //given
        Packs packs= new Packs(1L,
                "kk@o2.pl",
                "bb@o2.pl",
                "123456",
                Size.MEDIUM,
                Status.TO_RECEIVE,
                false,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),new ParcelLocker(),new User());

        //when
        Packs pack = underTest.findByPickupCode(packs.getPickupCode());
        //then
        assertThat(pack).isEqualTo(pack);
    }
}