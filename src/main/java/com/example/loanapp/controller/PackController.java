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
                                           @AuthenticationPrincipal User user) throws MessagingException, StripeException {

        Stripe.apiKey= "sk_test_51OHCJtDC7PH0QkQkgOZfQKT0MLhcPVSyTOX1lRLeeDwPpCDHmXmSVvyTWlW45jS1xN5gWBjfibWS6zr2RJdlEoTJ00nxzxvhYK";


        PaymentIntent paymentIntent = PaymentIntent.create(
                new PaymentIntentCreateParams.Builder()
                        .setCurrency("usd") // Lub inna waluta
                        .setAmount(1099L)    // Kwota w centach
                        .setReturnUrl("http://localhost:8080/payment/success")
                        .setConfirm(true)
                        .build()
        );



        if ("succeeded".equals(paymentIntent.getStatus())) {
            // Płatność zakończona sukcesem, aktualizuj status paczki
            pack.setUser(userService.findById(user.getId()));
            pack.setEmailSender(user.getEmail());
            pack.setDateOfPosting(LocalDateTime.now());
            pack.setExpirationDate(LocalDateTime.now().plusDays(7));
            pack.setStatus(Status.TO_RECEIVE);

            // Zapisz paczkę w bazie danych
            packsService.sendParcel(pack);

            emailService.sendEmailWhenPackIsSending(pack.getEmailReceiver(), pack.getPickupCode());

            return ResponseEntity.ok("Paczka wysłana");
        } else {
            // Płatność nie powiodła się, zwróć odpowiednią odpowiedź
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błąd płatności");
        }
    }
    @GetMapping("/receivePack/{pickupCode}")
    public ResponseEntity<String> receivePack(@PathVariable String pickupCode,
                                              @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(packsService.receivePack(pickupCode));


    }


}
