package com.intuit.craftDemo.repository;

import com.intuit.craftDemo.model.AuthenticationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TokenRepository extends JpaRepository<AuthenticationToken, Long> {
    Optional<AuthenticationToken> findTokenByUserId(Long userId);
    Optional<AuthenticationToken> findTokenByToken(String token);
}
