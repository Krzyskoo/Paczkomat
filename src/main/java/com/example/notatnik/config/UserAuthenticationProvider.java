package com.example.notatnik.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.notatnik.DTOS.LoginRequestDTO;
import com.example.notatnik.model.User;
import com.example.notatnik.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@Component
public class UserAuthenticationProvider {
    @Value("${security.jwt.token.secret-key:my-secret-key}")
    private String secretKey;

    private final UserService userService;

    public UserAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    protected void init(){
        secretKey= Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
    public String createToken(String login){
        Date now =new Date();
        Date validity = new Date(now.getTime()+3600000);

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withIssuer(login)
                .withIssuedAt(Instant.now())
                .withExpiresAt(validity)
                .sign(algorithm);
    }
    public Authentication validateToken (String token){
        Algorithm algorithm= Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded =verifier.verify(token);
        User user = userService.findByLogin(decoded.getIssuer());
        return new UsernamePasswordAuthenticationToken(user,null, Collections.emptyList());

    }
    public Authentication validateCredentials(LoginRequestDTO credentialsDTO){
        User user = userService.findByLogin(credentialsDTO.getLogin());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (user == null || !encoder.matches(credentialsDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }


}
