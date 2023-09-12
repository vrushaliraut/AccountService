package com.intuit.craftDemo.service;

import com.intuit.craftDemo.model.AuthenticationToken;
import com.intuit.craftDemo.repository.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    @Autowired
    TokenRepository tokenRepository;

    Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public void saveConfirmationToken(AuthenticationToken authenticationToken) {
        tokenRepository.save(authenticationToken);
    }

    public Optional<AuthenticationToken> getToken(Long userId) {
        return tokenRepository.findTokenByUserId(userId);
    }

    public Boolean invalidateToken(String token) {
        Optional<AuthenticationToken> authenticationToken = tokenRepository.findTokenByToken(token);
        logger.info("fetch auth token from repository :: " + authenticationToken.isPresent());
        if (authenticationToken.isPresent()) {
            AuthenticationToken authToken = new AuthenticationToken();
            authToken = authenticationToken.get();
            logger.info("authToken" + authToken);

            tokenRepository.delete(authToken);
            return true;
        }
        logger.info("error deleting auth token");
        return false;
    }
}
