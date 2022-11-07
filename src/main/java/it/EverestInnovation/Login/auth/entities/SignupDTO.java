package it.EverestInnovation.Login.auth.entities;

import lombok.Data;

@Data
public class SignupDTO {

    private String name;
    private String surname;


    private String email;
    /**Password in chiaro*/
    private String password;


}
