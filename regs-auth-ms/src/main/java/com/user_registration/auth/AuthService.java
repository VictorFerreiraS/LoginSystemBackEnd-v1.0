package com.user_registration.auth;

import com.user_registration.auth.requests.AuthenticationRequest;
import com.user_registration.auth.requests.RegisterRequest;
import com.user_registration.auth.responses.AuthResponse;
import com.user_registration.jwt.JwtService;
import com.user_registration.auth.dtos.EmailDto;
import com.user_registration.exceptions.UserAuthenticationException;
import com.user_registration.feignclients.EmailFeignClient;
import com.user_registration.token.TokenService;
import com.user_registration.user.Role;
import com.user_registration.user.User;
import com.user_registration.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final EmailFeignClient emailFeignClient;


//    Registration method
//    Builds a user checks to see if the email is not taken or empty
//    Generates a token
//    Returns a AuthResponse response JWT Token
public AuthResponse register(RegisterRequest request) throws UserAuthenticationException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAuthenticationException("Email Taken");
        }
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .confirmed(false)
                .role(Role.USER)
                .build();

        EmailDto confirmationEmail = EmailDto.builder()
                .ownerRef("Auth")
                .emailFrom("victor.fagundes586@gmail.com")
                .emailTo(request.getEmail())
                .subject("Email Confirmation")
                .text("Confirm you email here http://localhost:3000/confirm-email")
                .build();

//        Confirmation email with restTemplate.exchange;
//        exchange was used in order to receive a custom string response;
    try {
        ResponseEntity<String> emailResponse = emailFeignClient.sendEmail(confirmationEmail);

            if (emailResponse.getStatusCode().is2xxSuccessful()) {
                var savedUser = userRepository.save(user);
                var jwtToken = jwtService.generateToken(user);
                tokenService.saveUserToken(savedUser, jwtToken);
                return AuthResponse.builder().token(jwtToken).emailResponse(emailResponse.getBody()).build();
            } else {
                throw new UserAuthenticationException("Failed to send confirmation email");
            }
        } catch (Exception e) {
            throw new UserAuthenticationException("Failed to send confirmation email", e);
        }
    }

    //   Authentication method
//   uses authenticationManager to authenticate the request body in the http request
//   tests for the presence of the user using find by email
//   returns a jwtToken allowing request to retrieve the user information

    public AuthResponse authenticate(AuthenticationRequest request) throws UserAuthenticationException {
        // Input validation
        if (request.getEmail() == null || request.getEmail().isEmpty() ||
                request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new UserAuthenticationException("Email and password are required.");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception err) {
            throw new UserAuthenticationException("Authentication failed: " + err.getMessage());
        }

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserAuthenticationException("User not found"));

        var jwtToken = jwtService.generateToken(user);

        tokenService.revokeAllUserTokens(user);
        tokenService.deleteALlUserTokens(user);
        tokenService.saveUserToken(user, jwtToken);

        return AuthResponse.builder().token(jwtToken).build();
    }

}
