package com.intuit.craftDemo.service;

import com.intuit.craftDemo.model.AuthenticationToken;
import com.intuit.craftDemo.repository.TokenRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

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

    @Test
    public void testInvalidateTokenSuccessful() {
        String tokenValue = "validToken";
        AuthenticationToken authenticationToken = new AuthenticationToken();
        when(tokenRepository.findTokenByToken(tokenValue)).thenReturn(Optional.of(authenticationToken));

        Boolean result = authService.invalidateToken(tokenValue);

        assertTrue(result);
        verify(tokenRepository).findTokenByToken(tokenValue);
        verify(tokenRepository).delete(authenticationToken);
    }

    @Test
    public void testInvalidateTokenFailed() {
        String tokenValue = "invalidToken";
        AuthenticationToken authenticationToken = new AuthenticationToken();
        when(tokenRepository.findTokenByToken(tokenValue)).thenReturn(Optional.empty());

        Boolean result = authService.invalidateToken(tokenValue);

        assertFalse(result);
        verify(tokenRepository).findTokenByToken(tokenValue);
        verify(tokenRepository, never()).delete(authenticationToken);
    }
}