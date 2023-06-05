package com.sportsvault.repository;

import com.sportsvault.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    @Query("select u.balance from User u where u.id = :userId")
    Float getBalance(@Param("userId") UUID userId);
}
