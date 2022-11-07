package it.EverestInnovation.Login.notification.services;

import it.EverestInnovation.Login.user.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailNotificationService {

    @Autowired
    private JavaMailSender emailSender;
    public void sendActivationMail(User user) {
        SimpleMailMessage sms = new SimpleMailMessage();
        sms.setTo(user.getEmail());
        sms.setFrom("wellerman.test@gmail.com");
        sms.setReplyTo("wellerman.test@gmail.com");
        sms.setSubject("Ti sei iscritto alla piattaforma");
        sms.setText("Il codice di attivazione è: " + user.getActivationCode());

        emailSender.send(sms);


    }

    public void sendPasswordResetMail(User user) {
        SimpleMailMessage sms = new SimpleMailMessage();
        sms.setTo(user.getEmail());
        sms.setFrom("wellerman.test@gmail.com");
        sms.setReplyTo("wellerman.test@gmail.com");
        sms.setSubject("Ti sei iscritto alla piattaforma");
        sms.setText("Il codice di attivazione è: " + user.getPasswordResetCode());

        emailSender.send(sms);
    }
}
