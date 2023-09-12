package com.intuit.craftDemo.controller;

import com.intuit.craftDemo.dto.ResponseDto;
import com.intuit.craftDemo.dto.SignInResponseDto;
import com.intuit.craftDemo.dto.user.SignInDto;
import com.intuit.craftDemo.dto.user.SignupDto;
import com.intuit.craftDemo.exceptions.AuthenticationFailedException;
import com.intuit.craftDemo.exceptions.CustomException;
import com.intuit.craftDemo.service.AuthenticationService;
import com.intuit.craftDemo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Mock
    AuthenticationService authenticationService;

    @Test
    public void testSignUpValidInputSuccess() throws CustomException, NoSuchAlgorithmException {
        SignupDto signupDto = new SignupDto("alex", "bob", "valid@example.com", "password");
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseDto responseDto = new ResponseDto("success", "User registered successfully");
        when(userService.Signup(signupDto)).thenReturn(responseDto);

        ResponseEntity<ResponseDto> response = userController.Signup(signupDto, bindingResult);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("User registered successfully", response.getBody().getMessage());
    }

    @Test
    public void testSignUpInvalidInputBindingResultErrors() throws CustomException, NoSuchAlgorithmException {
        SignupDto signupDto = new SignupDto("cater", "cater", "validexampl", "password");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);


        FieldError fieldError = new FieldError("signUpDto", "email", "invalid email format");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        ResponseEntity<ResponseDto> response = userController.Signup(signupDto, bindingResult);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("error", response.getBody().getStatus());
    }

    @Test
    public void testSignInHasValidCredentialsSuccess() throws CustomException {
        SignInDto signInDto = new SignInDto("valid@example.com", "password");
        SignInResponseDto responseDto = new SignInResponseDto("success", "valid_token", "message");

        when(userService.SignIn(signInDto)).thenReturn(responseDto);

        ResponseEntity<SignInResponseDto> signInResponseDto = userController.SignIn(signInDto);
        assertNotNull(signInResponseDto);
    }

    @Test
    public void testForInvalidCredentialsInSignIn() {
        SignInDto signInDto = new SignInDto("invalid@example.com", "wrong_password");

        when(userService.SignIn(signInDto)).thenThrow(new CustomException("Invalid credentials"));

        assertThrows(CustomException.class, () -> userController.SignIn(signInDto));
    }

    @Test
    public void testLogoutValidTokenSuccessfulLogout() throws AuthenticationFailedException {
        String token = "valid_token";
        when(authenticationService.invalidateToken(token)).thenReturn(true);
        ResponseEntity<ResponseDto> response = userController.logout(token);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
    }

    @Test
    public void testLogout_InvalidToken_FailedLogout() throws AuthenticationFailedException {
        String token = "invalid_token";
        when(authenticationService.invalidateToken(token)).thenReturn(false);
        ResponseEntity<ResponseDto> response = userController.logout(token);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("error", response.getBody().getStatus());
    }

}