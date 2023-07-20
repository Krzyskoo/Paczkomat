package com.example.notatnik.DTOS;

import com.example.notatnik.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String login;

    public UserDTO(User user) {
        this.login = user.getLogin();
    }
}
