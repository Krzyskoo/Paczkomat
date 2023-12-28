package com.example.loanapp.repo;

import com.example.loanapp.model.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class PacksRepoTest {
    @Autowired
    private PacksRepo packsRepo;
    @Autowired
    private ParcelLockerRepo parcelLockerRepo;
    @Autowired
    private UserRepo userRepo;
    @Test
    @Transactional
    void shouldReturnPackWhoExpirationDateIsLessThanThreeHours() {
        //given
        Packs packs = new Packs(2L,"kkaa@o2.pl","oaod@o2.pl","12345",Size.MEDIUM,Status.TO_RECEIVE,false, LocalDateTime.now(),LocalDateTime.now().plusDays(1),createParcelLocker(),createUser());
        userRepo.save(createUser());
        parcelLockerRepo.save(createParcelLocker());
        packsRepo.save(packs);
        //when
        List<Packs> result = packsRepo.findByExpirationDateLessThan(LocalDateTime.now().plusDays(3));
        //then
        assertThat(result).contains(packs);


    }

    @Test
    @Transactional
    void shouldReturnPackByPickupCode() {
        //given
        Packs packs = new Packs(1L,"kkaa@o2.pl","oaod@o2.pl","12345",Size.MEDIUM,Status.TO_RECEIVE,false, LocalDateTime.now(),LocalDateTime.now().plusDays(5),createParcelLocker(),createUser());
        userRepo.save(createUser());
        parcelLockerRepo.save(createParcelLocker());

        packsRepo.save(packs);

        //when
        Packs result=packsRepo.findByPickupCode(packs.getPickupCode());
        //then
        assertThat(result).isNotNull();
        assertThat(result.getEmailSender()).isEqualTo("kkaa@o2.pl");
        assertThat(result.getEmailReceiver()).isEqualTo("oaod@o2.pl");}

    public User createUser(){
        return new User(1L,"krzystzof","Kandyba","kk@o2.pl","kkk", Role.USER);
    }
    public ParcelLocker createParcelLocker(){
        return new ParcelLocker(1L,1, Size.MEDIUM,true,false);
    }

}