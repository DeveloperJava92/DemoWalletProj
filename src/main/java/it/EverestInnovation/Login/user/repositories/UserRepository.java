package it.EverestInnovation.Login.user.repositories;

import it.EverestInnovation.Login.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    static void getByEmail(String email) {
    }

    User findByEmail(String email);

    User findByActivationCode(String activationCode);

    User findByPasswordResetCode(String passwordResetCode);
}
