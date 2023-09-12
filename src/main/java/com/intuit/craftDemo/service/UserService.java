package com.intuit.craftDemo.service;

import com.intuit.craftDemo.config.ResponseConstants;
import com.intuit.craftDemo.dto.ResponseDto;
import com.intuit.craftDemo.dto.SignInResponseDto;
import com.intuit.craftDemo.dto.user.SignInDto;
import com.intuit.craftDemo.dto.user.SignupDto;
import com.intuit.craftDemo.exceptions.CustomException;
import com.intuit.craftDemo.model.AuthenticationToken;
import com.intuit.craftDemo.model.User;
import com.intuit.craftDemo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static com.intuit.craftDemo.config.Constants.*;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    public ResponseDto Signup(SignupDto signupDto) throws CustomException {
        Optional<User> email = userRepository.findByEmail(signupDto.getEmail());
        if (email.isPresent()) {
            return new ResponseDto(ResponseConstants.ERROR, USER_ALREADY_EXISTS);
        }
        String encryptedPassword = signupDto.getPassword();
        try {
            encryptedPassword = hashPassword(signupDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        User user = new User(signupDto.getFirstname(), signupDto.getLastname(),
                signupDto.getEmail(), encryptedPassword);
        try {
            //generate token for user
            user = userRepository.save(user);
            AuthenticationToken authenticationToken = new AuthenticationToken(user.getId(), user.getEmail());

            //save token
            authenticationService.saveConfirmationToken(authenticationToken);
            logger.info("save token successfully");
            return new ResponseDto(ResponseConstants.SUCCESS, USER_CREATED);

        } catch (Exception e) {
            logger.debug("hashing failed" + HASHING_FAILED);
            return new ResponseDto(ResponseConstants.ERROR, HASHING_FAILED);
        }
    }

    public SignInResponseDto SignIn(SignInDto signInDto) {
        //find user by email
        Optional<User> userOptional = userRepository.findByEmail(signInDto.getEmail());
        if (userOptional.isEmpty()) {
            logger.info("user does not exists");
            return new SignInResponseDto(ResponseConstants.ERROR, EMPTY_TOKEN, USER_DOES_NOT_EXISTS);
        }

        //check password match
        User user = userOptional.get();
        logger.info("user info ::" + user.getEmail());
        try {
            if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))) {
                logger.info("authentication failed");
                return new SignInResponseDto(ResponseConstants.ERROR, EMPTY_TOKEN, WRONG_PASSWORD);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("hashing password failed : {}", e.getMessage());
            throw new CustomException(e.getMessage());
        }

        //call service
        Optional<AuthenticationToken> token = authenticationService.getToken(user.getId());
        if (token.isPresent()) {
            logger.info("user is already logged in");
            return new SignInResponseDto(ResponseConstants.ERROR, EMPTY_TOKEN, USER_HAS_BEEN_ALREADY_LOGGED_IN);
        }

        //save token in database
        AuthenticationToken authenticationToken = new AuthenticationToken(user.getId(), user.getEmail());
        authenticationService.saveConfirmationToken(authenticationToken);

        Optional<AuthenticationToken> newToken = authenticationService.getToken(user.getId());
        if (newToken.isPresent()) {
            logger.info("login successfully");
            AuthenticationToken authToken = newToken.get();
            return new SignInResponseDto(ResponseConstants.SUCCESS, authToken.getToken(), TOKEN);
        } else {
            logger.info("generate new token is empty");
            return new SignInResponseDto(ResponseConstants.SUCCESS, "", TOKEN);
        }
    }

    public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return myHash;
    }
}

