package com.vodafone.sportyshoes.dao;

import com.vodafone.sportyshoes.entities.User;
import com.vodafone.sportyshoes.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {
    Optional<User> findFirstByUserType(UserRole userType);

    Optional<User> findFirstByEmail(String email);

    List<User> findAllByNameContainingIgnoreCase(String query);

    List<User> findAllByDeletedFalse();

    int countByDeletedFalse();

    Optional<User> findFirstByEmailAndDeletedFalse(String email);
}
