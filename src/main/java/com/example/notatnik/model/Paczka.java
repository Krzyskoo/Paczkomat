package com.example.notatnik.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Paczka {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rozmiar;
    private String status;
    private LocalDateTime dataNadania;
    private LocalDateTime dataWaznosci;
    private String emailOdbiorcy;
    private LocalDateTime czasWaznosci;
    private boolean rememberMailSended;
    private String kodOdbioru;

    @ManyToOne
    @JoinColumn(name = "skrytka_id")
    private Paczkomat paczkomat;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Gettery, settery i konstruktory
}
