package com.example.loanapp.service;

import com.example.loanapp.model.*;
import com.example.loanapp.repo.PacksRepo;
import com.example.loanapp.repo.ParcelLockerRepo;
import com.example.loanapp.repo.UserRepo;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class PacksServiceTest {

    @InjectMocks
    private PacksService packsService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private ParcelLockerService parcelLockerService;

    @Mock
    private UserService userService;

    @Mock
    private ParcelLockerRepo parcelLockerRepo;
    @Mock
    private PacksRepo packsRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private EmailService emailService;

    @Test
    void shouldSendPack() {
        //given
        User user = createUser();
        user.setId(1L);
        user.setEmail("test@example.com");

        Packs packs = new Packs();
        packs.setSize(Size.SMALL);
        packs.setEmailReceiver("kk@o2.pl");

        ParcelLocker parcelLocker = createParcelLocker();
        parcelLockerRepo.save(parcelLocker);

        when(parcelLockerService.findFreeParcelLocker(eq(packs.getSize()))).thenReturn(parcelLocker);
        when(userService.findById(1L)).thenReturn(user);

        // when
        //packsService.sendParcel(packs, user);

        // then
        verify(parcelLockerService, times(1)).findFreeParcelLocker(eq(packs.getSize()));
        verify(parcelLockerService, times(1)).updateStatusParcelLocker(eq(parcelLocker.getId()), eq(false));
        verify(packsRepo, times(1)).save(any());

        assertEquals(parcelLocker, packs.getParcelLocker());  // Sprawdzenie, czy paczka ma ustawiony odpowiedni parcelLocker
        assertEquals(user, packs.getUser());  // Sprawdzenie, czy paczka ma ustawionego odpowiedniego użytkownika
        assertEquals(user.getEmail(), packs.getEmailSender());  // Sprawdzenie, czy paczka ma ustawiony odpowiedni adres email nadawcy
        assertEquals(Status.TO_RECEIVE, packs.getStatus());

    }

    @Test
    void shouldCreatePickupCode() {
        assertThat(packsService.createPickupCode()).isNotEmpty();
    }

    @Test
    void checkExpirationDates() {
        //given
        Packs expiredPack = new Packs();
        expiredPack.setStatus(Status.TO_RECEIVE);
        expiredPack.setReminderMessageSend(false);
        expiredPack.setExpirationDate(LocalDateTime.now().minusDays(1));

        Packs nonExpiredPack = new Packs();
        nonExpiredPack.setStatus(Status.TO_RECEIVE);
        nonExpiredPack.setReminderMessageSend(false);
        nonExpiredPack.setExpirationDate(LocalDateTime.now().plusDays(2));
        Packs nonExpiredPack2 = new Packs();
        nonExpiredPack2.setStatus(Status.TO_RECEIVE);
        nonExpiredPack2.setReminderMessageSend(false);
        nonExpiredPack2.setExpirationDate(LocalDateTime.now().plusDays(6));

        List<Packs> packsList = Arrays.asList(expiredPack, nonExpiredPack,nonExpiredPack2);

        when(packsRepo.findByExpirationDateLessThan(any(LocalDateTime.class)))
                .thenAnswer(invocation -> {
                    LocalDateTime expirationDateThreshold = invocation.getArgument(0);

                    // Filtruj dane z packsList zgodnie z wymaganiami
                    List<Packs> filteredPacks = packsList.stream()
                            .filter(pack -> pack.getExpirationDate().isBefore(expirationDateThreshold))
                            .collect(Collectors.toList());

                    return filteredPacks;
                });

        // when
        List<Packs> result = packsService.checkExpirationDates();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).contains(expiredPack);


    }

    @Test
    void sendReminder() throws MessagingException {

    }

    @Test
    void receivePack_ValidPickupCode_PackReceivedSuccessfully() {

    }


    @Test
    void checkPackStatus() {

    }

    public User createUser(){
        return new User(1L,"krzystzof","Kandyba","kk@o2.pl","kkk", Role.USER);
    }
    public ParcelLocker createParcelLocker(){
        return new ParcelLocker(1L,1, Size.MEDIUM,true,false);
    }


}