package com.example.loanapp.repo;

import com.example.loanapp.model.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

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
    @Commit
    void shouldReturnPackWhoExpirationDateIsLessThanThreeHours() {
        //given
        userRepo.save(createUser());
        ParcelLocker parcelLocker = createParcelLocker(); // Tworzymy obiekt ParcelLocker
        parcelLockerRepo.save(parcelLocker); // Zapisujemy obiekt ParcelLocker w bazie danych
        Packs packs = new Packs(1L, "kkaa@o2.pl", "oaod@o2.pl", "12345", Size.MEDIUM, Status.TO_RECEIVE, false, LocalDateTime.now(), LocalDateTime.now().plusDays(1), parcelLocker, createUser());
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
        userRepo.save(createUser());
        parcelLockerRepo.save(createParcelLocker());
        Packs packs = new Packs(1L,"kkaa@o2.pl","oaod@o2.pl","12345",Size.MEDIUM,Status.TO_RECEIVE,false, LocalDateTime.now(),LocalDateTime.now().plusDays(5),createParcelLocker(),createUser());


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
        return new ParcelLocker(2L,1, Size.MEDIUM,true,false);
    }

}