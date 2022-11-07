package it.EverestInnovation.Login.user.entities;

import lombok.Data;

@Data
public class LoginRTO {

    private User user;
    private String JWT;
}
