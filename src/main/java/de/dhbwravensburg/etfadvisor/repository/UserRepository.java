package de.dhbwravensburg.etfadvisor.repository;


import de.dhbwravensburg.etfadvisor.entity.Role;
import de.dhbwravensburg.etfadvisor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
   Optional<User> findByUsername(String username);

   List<User> findByRole(Role role);
}
