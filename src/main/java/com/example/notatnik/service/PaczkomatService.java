package com.example.notatnik.service;

import com.example.notatnik.repo.PaczkomatRepo;
import org.springframework.stereotype.Service;

@Service
public class PaczkomatService {
    PaczkomatRepo paczkomatRepo;

    public PaczkomatService(PaczkomatRepo paczkomatRepo) {
        this.paczkomatRepo = paczkomatRepo;
    }
}
