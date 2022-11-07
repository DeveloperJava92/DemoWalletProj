package it.EverestInnovation.Login.auth.entities;

import lombok.Data;

@Data
public class LoginDTO {
    /**Email*/
    private String email;
    /**Password*/
    private String password;

}
