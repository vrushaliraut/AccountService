package com.intuit.craftDemo.repository;

import com.intuit.craftDemo.model.AuthenticationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TokenRepository extends JpaRepository<AuthenticationToken, String> {
    Optional<String> findTokenByUserId(Long userId);

    Optional<String> findTokenByToken(String token);
}
