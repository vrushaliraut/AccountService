package com.intuit.craftDemo.controller;

import com.intuit.craftDemo.config.ResponseConstants;
import com.intuit.craftDemo.dto.ResponseDto;
import com.intuit.craftDemo.dto.user.SignupDto;
import com.intuit.craftDemo.exceptions.CustomException;
import com.intuit.craftDemo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/signup")
    public ResponseEntity<ResponseDto> Signup(@Validated @RequestBody SignupDto signupDto,
                                              BindingResult bindingResult) throws CustomException, NoSuchAlgorithmException {
        if (bindingResult.hasErrors()) {
            logger.info("binding errors");
            return ResponseEntity.badRequest().body(getResponseDto(bindingResult));
        }
        logger.info("no binding errors");
        ResponseDto responseDto = userService.Signup(signupDto);
        return ResponseEntity.ok(responseDto);
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
