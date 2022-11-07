package it.EverestInnovation.Login;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import it.EverestInnovation.Login.auth.services.LoginService;
import it.EverestInnovation.Login.user.entities.User;
import it.EverestInnovation.Login.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
        throws ServletException, IOException{
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")){
            chain.doFilter(request, response);
            return;
        }

        final String token = header.trim();
        DecodedJWT decoded;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(LoginService.JWT_SECRET)).withIssuer("Matteo demo").build();
            decoded = verifier.verify(token);
        }catch (JWTVerificationException ex){
            return;
        }


        Optional<User> userDetails = userRepo.findById(decoded.getClaim("id").asLong());
        if (!userDetails.isPresent() || !userDetails.get().isActive()){
            chain.doFilter(request,response);
            return;
        }

        User user = userDetails.get();
        user.setPassword(null);
        user.setActivationCode(null);
        user.setPasswordResetCode(null);

        //Autenticazione è dato dall'utente e se ha le credenziali ha una lista di permessi,
        // per ora vuota

        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(
                userDetails.get(), null,
                List.of()
        );
        //codice standard sulla request
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        //scrivo la mia autenticazione mandandola avanti con il doFilter
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request,response);

    }

}
