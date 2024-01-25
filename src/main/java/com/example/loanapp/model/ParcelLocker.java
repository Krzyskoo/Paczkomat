package com.example.loanapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParcelLocker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int number;
    @Enumerated(EnumType.STRING)
    private Size size;

    private boolean isFree;

    private boolean returnToSender;

    @OneToMany(mappedBy = "parcelLocker")
    private List<Packs> packs;

    public ParcelLocker(Long id, int number, Size size, boolean isFree, boolean returnToSender) {
        this.id = id;
        this.number = number;
        this.size = size;
        this.isFree = isFree;
        this.returnToSender = returnToSender;
    }


}
