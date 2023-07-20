package com.example.notatnik.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Paczkomat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numer;
    private String rozmiarPaczki;
    private String status;
    @OneToMany(mappedBy = "paczkomat")
    private List<Paczka> paczki;
    // Gettery, settery i konstruktory
}
