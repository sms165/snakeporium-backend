package com.snakeporium_backend.repository;

import com.snakeporium_backend.entity.User;
import com.snakeporium_backend.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long userId);

    User findByRole(UserRole userRole);
}
