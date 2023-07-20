package com.example.notatnik.controller;

import com.example.notatnik.DTOS.LoginResponseDTO;
import com.example.notatnik.config.UserAuthenticationProvider;
import com.example.notatnik.model.User;
import com.example.notatnik.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class AuthController {

    private final UserAuthenticationProvider userAuthenticationProvider;
    private final UserService userService;

    public AuthController(UserAuthenticationProvider userAuthenticationProvider, UserService userService) {
        this.userAuthenticationProvider = userAuthenticationProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@AuthenticationPrincipal User user){
        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setToken(userAuthenticationProvider.createToken(user.getLogin()));
        return ok(responseDTO);
    }

}
