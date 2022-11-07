package it.EverestInnovation.Login.auth.services;

import it.EverestInnovation.Login.auth.entities.RequestPasswordDTO;
import it.EverestInnovation.Login.auth.entities.RestorePasswordDTO;
import it.EverestInnovation.Login.user.entities.User;
import it.EverestInnovation.Login.user.repositories.UserRepository;
import it.EverestInnovation.Login.notification.services.MailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailNotificationService mailNotificationService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public void request(RequestPasswordDTO requestPasswordDTO) throws Exception {
        User userFromDB = userRepository.findByEmail(requestPasswordDTO.getEmail());
        if (userFromDB == null) throw new Exception("Utente inesistente");
        userFromDB.setPasswordResetCode(UUID.randomUUID().toString());
        mailNotificationService.sendPasswordResetMail(userFromDB);
        userRepository.save(userFromDB);
    }

    public User restore(RestorePasswordDTO restorePasswordDTO) throws Exception {
        User userFromDB = userRepository.findByPasswordResetCode(restorePasswordDTO.getResetPasswordCode());
        if(userFromDB == null) throw new Exception("Utente inesistente");
        userFromDB.setPassword(passwordEncoder.encode(restorePasswordDTO.getNewPassword()));
        userFromDB.setPasswordResetCode(null);

        userFromDB.setActive(true);
        userFromDB.setActivationCode(null);

        return userRepository.save(userFromDB);
    }
}
