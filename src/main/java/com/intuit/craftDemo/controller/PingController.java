package com.intuit.craftDemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping("/ping")
    public String Ping() {
        String message = new String("pong");
        return message;
    }
}
