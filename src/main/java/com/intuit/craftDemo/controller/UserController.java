package com.intuit.craftDemo.controller;

import com.intuit.craftDemo.config.ResponseConstants;
import com.intuit.craftDemo.dto.ResponseDto;
import com.intuit.craftDemo.dto.SignInResponseDto;
import com.intuit.craftDemo.dto.user.SignInDto;
import com.intuit.craftDemo.dto.user.SignupDto;
import com.intuit.craftDemo.exceptions.AuthenticationFailedException;
import com.intuit.craftDemo.exceptions.CustomException;
import com.intuit.craftDemo.service.AuthenticationService;
import com.intuit.craftDemo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationService authenticationService;
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/signup")
    public ResponseEntity<ResponseDto> Signup(@Validated @RequestBody SignupDto signupDto,
                                              BindingResult bindingResult) throws CustomException, NoSuchAlgorithmException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(getResponseDto(bindingResult));
        }
        ResponseDto responseDto = userService.Signup(signupDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/signIn")
    public ResponseEntity<SignInResponseDto> SignIn(@Validated @RequestBody SignInDto signInDto) throws CustomException {
        SignInResponseDto responseDto = userService.SignIn(signInDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/logout")
    public ResponseEntity<ResponseDto> logout(@RequestParam(value = "token", required = true) String token) throws AuthenticationFailedException {
        boolean success = authenticationService.invalidateToken(token);
        if (success) {
            logger.info("invalidated token successfully");
            ResponseDto logoutSuccessful = new ResponseDto(ResponseConstants.SUCCESS, "logout successful");
            return ResponseEntity.ok(logoutSuccessful);
        } else {
            logger.info("invalidated token failed");
            ResponseDto logoutFailed = new ResponseDto(ResponseConstants.ERROR, "logout failed");
            return ResponseEntity.badRequest().body(logoutFailed);
        }
    }


    public static ResponseDto getResponseDto(BindingResult bindingResult) {
        return new ResponseDto(ResponseConstants.ERROR, signupBindingError(bindingResult));
    }

    private static String signupBindingError(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(";"));
    }
}
