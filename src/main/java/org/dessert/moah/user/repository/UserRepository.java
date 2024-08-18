package org.dessert.moah.user.repository;

import jakarta.persistence.LockModeType;
import org.dessert.moah.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    //Optional<Users> findByName(String username);


    Optional<Users> findByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM Users u WHERE u.email = :email")
    Optional<Users> findByEmailWithLock(@Param("email") String email);

    Users findByName(String username);
}
