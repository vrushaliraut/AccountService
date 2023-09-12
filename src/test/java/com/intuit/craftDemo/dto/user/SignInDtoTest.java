package com.intuit.craftDemo.dto.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class SignInDtoTest {

    @Test
    public void testGettersAndSetters() {

        String email = "john@example.com";
        String password = "password123";

        SignInDto signInDto = new SignInDto(email, password);

        // Act
        String getEmailResult = signInDto.getEmail();
        String getPasswordResult = signInDto.getPassword();

        // Assert
        assertEquals(email, getEmailResult);
        assertEquals(password, getPasswordResult);
    }

    @Test
    public void testSetters() {
        // Arrange
        SignInDto signInDto = new SignInDto("", "");

        // Act
        signInDto.setEmail("jane@example.com");
        signInDto.setPassword("newPassword");

        // Assert
        assertEquals("jane@example.com", signInDto.getEmail());
        assertEquals("newPassword", signInDto.getPassword());
    }
}