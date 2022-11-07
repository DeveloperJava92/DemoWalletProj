package it.EverestInnovation.Login.auth.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import it.EverestInnovation.Login.auth.entities.LoginDTO;
import it.EverestInnovation.Login.user.entities.LoginRTO;
import it.EverestInnovation.Login.user.entities.User;
import it.EverestInnovation.Login.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class LoginService {

    public static final String JWT_SECRET = "74142491-e729-4d30-aaf4-f0b898c85a8e";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    public LoginRTO login(LoginDTO loginDTO){
        if(loginDTO == null)return null;
        User userFromDB = userRepository.findByEmail(loginDTO.getEmail());
        if(userFromDB == null || !userFromDB.isActive()) return null;

        boolean canLogin = this.canUserLogin(userFromDB, loginDTO.getPassword());
        if(!canLogin) return null;

        String JWT = getJWT(userFromDB);

        /**Salvo quando il JWT viene staccato*/
        userFromDB.setJwtCreatedOn(LocalDateTime.now());
        userRepository.save(userFromDB);

        userFromDB.setPassword(null);
        LoginRTO out = new LoginRTO();
        out.setJWT(JWT);
        out.setUser(userFromDB);

        return out;
    }
    /**Controllo se l'utente può loggare*/
    public boolean canUserLogin(User user, String password){

        return passwordEncoder.matches(password, user.getPassword());
    }

    static Date convertToDateViaInstant(LocalDateTime dateToConvert){
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public static String getJWT(User user){
        Date expiresAt = convertToDateViaInstant(LocalDateTime.now().plusDays(15));
        try {
            return JWT.create().withIssuer("Matteo demo").withIssuedAt(new Date()).withExpiresAt(expiresAt).withClaim("id", user.getId()).sign(Algorithm.HMAC512(JWT_SECRET));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }




    public String generateJWT(User user){
        String JWT = getJWT(user);

        user.setJwtCreatedOn(LocalDateTime.now());

        userRepository.save(user);

        return JWT;
    }
}
