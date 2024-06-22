package org.dessert.moah.repository;

import org.dessert.moah.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByName(String username);

    Optional<Users> findByEmail(String email);
}
