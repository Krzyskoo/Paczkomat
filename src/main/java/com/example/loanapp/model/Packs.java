package com.example.loanapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Packs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String emailSender;
    private String emailReceiver;

    private String pickupCode;
    @Enumerated(EnumType.STRING)
    private Size size;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Boolean reminderMessageSend;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dateOfPosting;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime expirationDate;
    @ManyToOne
    @JoinColumn(name = "ParcelLocker_id")
    private ParcelLocker parcelLocker;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}


