package com.intuit.craftDemo.service;

import com.intuit.craftDemo.config.Constants;
import com.intuit.craftDemo.config.ResponseConstants;
import com.intuit.craftDemo.dto.ResponseDto;
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
            return new ResponseDto(ResponseConstants.ERROR, Constants.USER_ALREADY_EXISTS);
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
            AuthenticationToken authenticationToken = new AuthenticationToken(user.getId());

            //save token
            authenticationService.saveConfirmationToken(authenticationToken);
            logger.info("save token successfully");
            return new ResponseDto(ResponseConstants.SUCCESS, Constants.USER_CREATED);

        } catch (Exception e) {
            logger.debug("hashing failed" + Constants.HASHING_FAILED);
            return new ResponseDto(ResponseConstants.ERROR, Constants.HASHING_FAILED);
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

