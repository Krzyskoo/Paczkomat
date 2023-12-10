package com.example.loanapp.service;

import com.example.loanapp.model.User;
import com.example.loanapp.repo.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    public User GetUserByEmail(String email){
        return userRepo.findByEmail(email).orElseThrow();
    }
    public User findById(Long id) {
        return userRepo.findById(id);
    }

}
