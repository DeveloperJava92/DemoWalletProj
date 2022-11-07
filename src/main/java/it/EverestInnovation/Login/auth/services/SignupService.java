package it.EverestInnovation.Login.auth.services;

import it.EverestInnovation.Login.auth.entities.SignupActivationDTO;
import it.EverestInnovation.Login.auth.entities.SignupDTO;
import it.EverestInnovation.Login.user.entities.User;
import it.EverestInnovation.Login.user.repositories.RoleRepository;
import it.EverestInnovation.Login.user.repositories.UserRepository;
import it.EverestInnovation.Login.notification.services.MailNotificationService;
import it.EverestInnovation.Login.user.utils.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class SignupService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MailNotificationService mailNotificationService;
    public User signup(SignupDTO signupDTO) throws Exception {
        User userInDB = userRepository.findByEmail(signupDTO.getEmail());
        if (userInDB != null ) throw new Exception("User già esistente");
        User user = new User();
        user.setName(signupDTO.getName());
        user.setEmail(signupDTO.getEmail());
        user.setSurname(signupDTO.getSurname());
        user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        user.setActive(false);//per evitare che utenti si registrino con email false
        user.setActivationCode(UUID.randomUUID().toString());

        Set<Role> roles = new HashSet<>();
        Optional<Role> userRole = roleRepository.findByName(Roles.REGISTERED);
        if (userRole.isPresent())throw new Exception("Cannot set role");
        roles.add(userRole.get());
        user.setRoles(roles);

        mailNotificationService.sendActivationMail(user);
        return userRepository.save(user);
    }

    /**Trovo l'utente attraverso l'activation code. Questo A.C. è univoco. Legato a questo code c'è un utente con email associata.
     * gli attivo il codice e abilito l'utente*/
    public User activate(SignupActivationDTO signupActivationDTO) throws Exception {
        User user = userRepository.findByActivationCode(signupActivationDTO.getActivationCode());
        if (user == null) throw new Exception("Utente non trovato");
        user.setActive(true);
        user.setActivationCode(null);
        return userRepository.save(user);
    }
}
