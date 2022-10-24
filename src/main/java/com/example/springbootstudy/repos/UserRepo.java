package com.example.springbootstudy.repos;

import com.example.springbootstudy.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {

    User findByUsername(String username);

    User findByActivationCode(String code);

    @Override
    Optional<User> findById(Long id);
}
