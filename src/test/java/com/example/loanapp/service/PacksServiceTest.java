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

    }

    @Test
    void shouldCreatePickupCode() {
        assertThat(packsService.createPickupCode()).isNotEmpty();
    }

    @Test
    void checkExpirationDates() {
        //given



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