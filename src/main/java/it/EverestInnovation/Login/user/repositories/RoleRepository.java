package it.EverestInnovation.Login.user.repositories;

import it.EverestInnovation.Login.user.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<javax.management.relation.Role> findByName(String name); //posso cercare i ruoli con il nome
}
