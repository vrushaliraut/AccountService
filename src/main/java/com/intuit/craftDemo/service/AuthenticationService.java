package com.intuit.craftDemo.service;

import com.intuit.craftDemo.model.AuthenticationToken;
import com.intuit.craftDemo.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    @Autowired
    TokenRepository tokenRepository;

    public void saveConfirmationToken(AuthenticationToken authenticationToken) {
        tokenRepository.save(authenticationToken);
    }

    public Optional<String> getToken(Long userId) {
        return tokenRepository.findTokenByUserId(userId);
    }

}
