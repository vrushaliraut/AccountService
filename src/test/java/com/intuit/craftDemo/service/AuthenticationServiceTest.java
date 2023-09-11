package com.intuit.craftDemo.service;

import com.intuit.craftDemo.model.AuthenticationToken;
import com.intuit.craftDemo.repository.TokenRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {
    @Mock
    TokenRepository tokenRepository;

    @InjectMocks
    AuthenticationService authService;

    @Test
    public void testAuthenticationServiceShouldSaveConfirmationToken() {
        AuthenticationToken authenticationToken = new AuthenticationToken();

        authService.saveConfirmationToken(authenticationToken);

        verify(tokenRepository).save(authenticationToken);
    }
}