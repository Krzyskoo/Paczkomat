package com.example.loanapp.controller;

import com.example.loanapp.exeptions.EmailExceptionByClient;
import com.example.loanapp.model.Packs;
import com.example.loanapp.model.User;
import com.example.loanapp.service.EmailService;
import com.example.loanapp.service.PacksService;
import com.example.loanapp.service.ParcelLockerService;
import com.example.loanapp.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vi")
public class PackController {

    ParcelLockerService parcelLockerService;
    PacksService packsService;
    UserService userService;
    EmailService emailService;

    public PackController(ParcelLockerService parcelLockerService,
                          PacksService packsService,
                          UserService userService,
                          EmailService emailService) {
        this.parcelLockerService = parcelLockerService;
        this.packsService = packsService;
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping("/parcel")
    public ResponseEntity<?> sendPack(@RequestBody Packs pack,
                                           @AuthenticationPrincipal User user) {
        try {
            packsService.sendParcel(pack, user);
            return ResponseEntity.ok().body("Paczka wysłana");
        } catch (MailSendException | MessagingException e) {
            throw new EmailExceptionByClient();
        }
        //request do poprawy w przypadku podania złego adresu email, który nie istnieje
    }
    @GetMapping("/parcel/{pickupCode}")
    public ResponseEntity<String> receivePack(@PathVariable String pickupCode,
                                              @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(packsService.receivePack(pickupCode));


    }


}
