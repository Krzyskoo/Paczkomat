package com.example.loanapp.controller;

import com.example.loanapp.model.Packs;
import com.example.loanapp.model.ParcelLocker;
import com.example.loanapp.model.Status;
import com.example.loanapp.model.User;
import com.example.loanapp.service.EmailService;
import com.example.loanapp.service.PacksService;
import com.example.loanapp.service.ParcelLockerService;
import com.example.loanapp.service.UserService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.mail.MessagingException;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

    @PostMapping("/sendParcel")
    public ResponseEntity<String> sendPack(@RequestBody Packs pack,
                                           @AuthenticationPrincipal User user) throws MessagingException {

            packsService.sendParcel(pack,user);

            emailService.sendEmailWhenPackIsSending(pack.getEmailReceiver(), pack.getPickupCode());

            return ResponseEntity.ok("Paczka wysłana");
        }
    @GetMapping("/receivePack/{pickupCode}")
    public ResponseEntity<String> receivePack(@PathVariable String pickupCode,
                                              @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(packsService.receivePack(pickupCode));


    }


}
