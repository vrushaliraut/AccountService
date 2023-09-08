package com.intuit.craftDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountService {
    public static void main(String[] args) {
        System.out.println("Hello account service");
        SpringApplication.run(AccountService.class, args);
    }
}
