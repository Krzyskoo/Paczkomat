package com.example.notatnik.service;

import com.example.notatnik.model.User;
import com.example.notatnik.repo.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    public User findByLogin(String username){
        return userRepo.findByLogin(username);
    }

}
