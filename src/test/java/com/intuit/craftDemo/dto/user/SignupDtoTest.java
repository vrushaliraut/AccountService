package com.intuit.craftDemo.dto.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class SignupDtoTest {

    @InjectMocks
    SignupDto signupDto;

    @Test
    public void testValidation_FirstNameEmpty() {
        SignupDto signupDto = new SignupDto("", "Doe", "john@example.com", "password");

        assertTrue(signupDto.getFirstname().isEmpty());
    }

    @Test
    public void testValidationEmailEmpty() {
        SignupDto signupDto = new SignupDto("John", "Doe", "", "password");
        assertTrue(signupDto.getEmail().isEmpty());
    }

    @Test
    public void testValidationPasswordEmpty() {
        SignupDto signupDto = new SignupDto("John", "Doe", "john@example.com", "");

        assertTrue(signupDto.getPassword().isEmpty());
    }

}