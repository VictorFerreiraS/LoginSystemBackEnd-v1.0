package com.user_registration.user;


import com.user_registration.auth.dtos.EmailDto;
import com.user_registration.exceptions.ChangePasswordException;
import com.user_registration.exceptions.SendingEmailException;
import com.user_registration.feignclients.EmailFeignClient;
import com.user_registration.jwt.JwtService;
import com.user_registration.exceptions.GettingTokenException;
import com.user_registration.exceptions.GettingUserException;
import com.user_registration.token.Token;
import com.user_registration.token.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final JwtService jwtService;
    private final EmailFeignClient emailFeignClient;
    private final PasswordEncoder passwordEncoder;



    public Optional<User> getUserDataWithToken(String tokenString) {
        String userEmail = jwtService.extractUsername(tokenService.removeBearerFromToken(tokenString));
        return userRepository.findByEmail(userEmail);
    }

    public void deleteUserByToken(String tokenString) throws GettingUserException, GettingTokenException {
        User user = getUserDataWithToken(tokenString).orElseThrow(() -> new GettingUserException("User could not be get;"));
        Token token = tokenService.getTokenWithTokenString(tokenString).orElseThrow(() -> new GettingTokenException("Token could not be get;"));
        tokenService.deleteTokenByTokenId(token.getId());
        userRepository.deleteById(user.getId());
    }

    public void sendConfirmationEmail(String tokenString) throws GettingUserException, SendingEmailException {
        User user = getUserDataWithToken(tokenString).orElseThrow(() -> new GettingUserException("User could not be get;"));

            EmailDto confirmationEmail = EmailDto.builder()
                    .ownerRef("Auth")
                    .emailFrom("victor.fagundes586@gmail.com")
                    .emailTo(user.getEmail())
                    .subject("Email Confirmation")
                    .text("Confirm you email here http://localhost:3000/confirm-email")
                    .build();

        ResponseEntity<String> emailResponse = emailFeignClient.sendEmail(confirmationEmail);

        if (!emailResponse.getStatusCode().is2xxSuccessful()) {
        throw new SendingEmailException("email could not be sent");
        }
    }

    public void changeUserPassword(String tokenString, String oldPassword, String newPassword) throws GettingUserException, ChangePasswordException {
        User user = getUserDataWithToken(tokenString).orElseThrow(() -> new GettingUserException("User could not be get;"));
        int userId = user.getId();
        try {
        int updatedCount = userRepository.updatePasswordWithOld(userId, passwordEncoder.encode(oldPassword), passwordEncoder.encode(newPassword));
        if (updatedCount == 0) {
            throw new ChangePasswordException("Old password is incorrect");
        }
    } catch (ChangePasswordException exception) {
        throw  new ChangePasswordException("Unable to change password");
    }

    }

}

