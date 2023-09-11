package com.intuit.craftDemo.repository;

import com.intuit.craftDemo.model.AuthenticationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TokenRepository extends JpaRepository<AuthenticationToken, String> {
    AuthenticationToken findTokenByUserId(Long userId);

    AuthenticationToken findTokenByToken(String token);
}
