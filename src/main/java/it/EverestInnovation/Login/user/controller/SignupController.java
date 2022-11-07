package it.EverestInnovation.Login.user.controller;

import it.EverestInnovation.Login.auth.entities.SignupActivationDTO;
import it.EverestInnovation.Login.auth.entities.SignupDTO;
import it.EverestInnovation.Login.auth.services.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class SignupController {

    @Autowired
    private SignupService signupService;

    @PostMapping("/signup")
    public void signup(@RequestBody SignupDTO signupDTO) throws Exception {
        signupService.signup(signupDTO);

    }

    @PostMapping("/signup/activation")
    public void signup(@RequestBody SignupActivationDTO signupActivationDTO) throws Exception {
        signupService.activate(signupActivationDTO);

    }


}
