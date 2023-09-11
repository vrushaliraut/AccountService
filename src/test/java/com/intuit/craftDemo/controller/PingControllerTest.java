package com.intuit.craftDemo.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class PingControllerTest {
    @InjectMocks
    private PingController pingController;

    @Test
    public void testPingShouldReturnPong() {
        String message = pingController.Ping();

        assertEquals("pong", message);
    }
}