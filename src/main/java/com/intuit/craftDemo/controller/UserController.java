package com.intuit.craftDemo.controller;

import com.intuit.craftDemo.config.ResponseConstants;
import com.intuit.craftDemo.dto.ResponseDto;
import com.intuit.craftDemo.dto.SignInResponseDto;
import com.intuit.craftDemo.dto.user.SignInDto;
import com.intuit.craftDemo.dto.user.SignupDto;
import com.intuit.craftDemo.exceptions.AuthenticationFailedException;
import com.intuit.craftDemo.exceptions.CustomException;
import com.intuit.craftDemo.model.User;
import com.intuit.craftDemo.service.AuthenticationService;
import com.intuit.craftDemo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<ResponseDto> signup(@Validated @RequestBody SignupDto signupDto,
                                              BindingResult bindingResult) throws CustomException, NoSuchAlgorithmException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(getResponseDto(bindingResult));
        }
        ResponseDto responseDto = userService.signup(signupDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/signIn")
    public ResponseEntity<SignInResponseDto> signIn(@Validated @RequestBody SignInDto signInDto) throws CustomException {
        SignInResponseDto responseDto = userService.signIn(signInDto);
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

    @GetMapping("/fetch/users")
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userService.getAllUsers(pageable);
        return ResponseEntity.ok(userPage);
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
