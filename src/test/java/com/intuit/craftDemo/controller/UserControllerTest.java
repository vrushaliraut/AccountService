package com.intuit.craftDemo.controller;

import com.intuit.craftDemo.dto.ResponseDto;
import com.intuit.craftDemo.dto.user.SignupDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

}