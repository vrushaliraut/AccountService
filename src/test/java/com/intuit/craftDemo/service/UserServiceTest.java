package com.intuit.craftDemo.service;

import com.intuit.craftDemo.config.Constants;
import com.intuit.craftDemo.config.ResponseConstants;
import com.intuit.craftDemo.dto.ResponseDto;
import com.intuit.craftDemo.dto.SignInResponseDto;
import com.intuit.craftDemo.dto.user.SignInDto;
import com.intuit.craftDemo.dto.user.SignupDto;
import com.intuit.craftDemo.model.AuthenticationToken;
import com.intuit.craftDemo.model.User;
import com.intuit.craftDemo.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static com.intuit.craftDemo.config.Constants.USER_DOES_NOT_EXISTS;
import static com.intuit.craftDemo.config.Constants.USER_HAS_BEEN_ALREADY_LOGGED_IN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignupSuccess() throws NoSuchAlgorithmException {
        SignupDto signupDto = new SignupDto("John", "Doe", "john@example.com", "password");
        User user = new User("John", "Doe", "john@example.com", "password");

        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseDto response = userService.signup(signupDto);

        assertEquals(ResponseConstants.SUCCESS, response.getStatus());
        assertEquals("user created successfully", response.getMessage());
        verify(userRepository, times(1)).findByEmail(signupDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testOnSignUpUserAlreadyExists() {
        SignupDto signupDto = new SignupDto("John", "Doe", "john@example.com", "password");
        User user = new User("John", "Doe", "john@example.com", "password");

        // Mocking userRepository findByEmail to return an Optional containing a user
        when(userRepository.findByEmail(signupDto.getEmail())).thenReturn(Optional.of(user));

        ResponseDto response = userService.signup(signupDto);

        assertEquals(ResponseConstants.ERROR, response.getStatus());
        assertEquals("user already exists", response.getMessage());
        verify(userRepository, times(1)).findByEmail(signupDto.getEmail());
        verify(userRepository, never()).save(any(User.class));
        assertTrue(userRepository.findByEmail(signupDto.getEmail()).isPresent());
    }


    @Test
    public void testEmailDoesNotExistsWhenUserTryToLogin() throws NoSuchAlgorithmException {
        SignInDto signInDto = new SignInDto("john@example.com", "password");

        when(userRepository.findByEmail(signInDto.getEmail())).thenReturn(Optional.empty());

        SignInResponseDto response = userService.signIn(signInDto);

        assertEquals(ResponseConstants.ERROR, response.getStatus());
        assertEquals(USER_DOES_NOT_EXISTS, response.getMessage());
        verify(userRepository, times(1)).findByEmail(signInDto.getEmail());
    }

    @Test
    public void testUserIsAlreadyLoggedIn() throws NoSuchAlgorithmException {
        SignInDto signInDto = new SignInDto("john@example.com", "password123");
        String hashPassword = userService.hashPassword(signInDto.getPassword());
        User user = new User("John", "Doe", "john@example.com", hashPassword);

        when(userRepository.findByEmail(signInDto.getEmail())).thenReturn(Optional.of(user));

        when(authenticationService.getToken(user.getId())).thenReturn(Optional.of(new AuthenticationToken()));

        // Act
        SignInResponseDto response = userService.signIn(signInDto);

        // Assert
        assertEquals(ResponseConstants.ERROR, response.getStatus());
        assertEquals(USER_HAS_BEEN_ALREADY_LOGGED_IN, response.getMessage());

        // Verify that userRepository.findByEmail and authenticationService methods were called
        verify(userRepository).findByEmail(signInDto.getEmail());
        verify(authenticationService).getToken(user.getId());
        verify(authenticationService, never()).saveConfirmationToken(any());
    }

    @Test
    public void testUserIsLoggedIn() throws NoSuchAlgorithmException {
        SignInDto signInDto = new SignInDto("john@example.com", "password123");
        String hashPassword = userService.hashPassword(signInDto.getPassword());
        User user = new User("John", "Doe", "john@example.com", hashPassword);

        // Mock userRepository behavior
        when(userRepository.findByEmail(signInDto.getEmail())).thenReturn(Optional.of(user));

        doNothing().when(authenticationService).saveConfirmationToken(any());

        when(authenticationService.getToken(user.getId()))
                .thenReturn(Optional.empty())  // First call returns empty
                .thenReturn(Optional.of(new AuthenticationToken(user.getId(), user.getEmail()))); // Second call returns a token

        // Act
        SignInResponseDto response = userService.signIn(signInDto);

        // Assert
        assertEquals(ResponseConstants.SUCCESS, response.getStatus());
        assertNotNull(response.getToken());
        assertEquals("user token", response.getMessage());

        // Verify that userRepository.findByEmail and authenticationService methods were called
        verify(userRepository).findByEmail(signInDto.getEmail());
        verify(authenticationService, times(2)).getToken(user.getId());
        verify(authenticationService).saveConfirmationToken(any());
    }

    @Test
    public void testDriverNotFoundWhileSignIn() {
        SignInDto signInDto = new SignInDto("john@example.com", "password");
        when(userRepository.findByEmail(signInDto.getEmail())).thenReturn(Optional.empty());

        SignInResponseDto signInResponseDto = userService.signIn(signInDto);

        assertEquals(Constants.USER_DOES_NOT_EXISTS, signInResponseDto.getMessage());
    }
}