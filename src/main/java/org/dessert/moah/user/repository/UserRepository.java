package org.dessert.moah.user.repository;

import org.dessert.moah.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    //Optional<Users> findByName(String username);

    Optional<Users> findByEmail(String email);

    Users findByName(String username);
}
