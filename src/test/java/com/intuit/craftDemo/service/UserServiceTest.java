package com.intuit.craftDemo.service;

import com.intuit.craftDemo.config.ResponseConstants;
import com.intuit.craftDemo.dto.ResponseDto;
import com.intuit.craftDemo.dto.user.SignupDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

        ResponseDto response = userService.Signup(signupDto);

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

        ResponseDto response = userService.Signup(signupDto);

        assertEquals(ResponseConstants.ERROR, response.getStatus());
        assertEquals("user already exists", response.getMessage());

        verify(userRepository, times(1)).findByEmail(signupDto.getEmail());
        verify(userRepository, never()).save(any(User.class));
        assertTrue(userRepository.findByEmail(signupDto.getEmail()).isPresent());
    }

}