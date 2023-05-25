package com.user_registration.auth;

import com.user_registration.auth.requests.AuthenticationRequest;
import com.user_registration.auth.requests.RegisterRequest;
import com.user_registration.auth.responses.AuthResponse;
import com.user_registration.config.JwtService;
import com.user_registration.token.Token;
import com.user_registration.token.TokenRepository;
import com.user_registration.token.TokenType;
import com.user_registration.user.Role;
import com.user_registration.user.User;
import com.user_registration.user.UserRepository;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;


    //    Registration method
//    Builds a user checks to see if the email is not taken or empty
//    Generates a token
//    Returns a AuthResponse response JWT Token
    public AuthResponse register(RegisterRequest request) throws AuthException {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .confirmed(false)
                .role(Role.USER)
                .build();


        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
            var savedUser = userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            saveUserToken(savedUser, jwtToken);
            return AuthResponse.builder().token(jwtToken).build();
        } else {
            throw new AuthException("Email Taken");
        }

    }

    //   Authentication method
//   uses authenticationManager to authenticate the request body in the http request
//   tests for the presence of the user using find by email
//   returns a jwtToken allowing request to retrieve the user information
    public AuthResponse authenticate(AuthenticationRequest request) throws AuthenticationException {
        // Input validation
        if (request.getEmail() == null || request.getEmail().isEmpty() ||
                request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new AuthenticationException("Email and password are required.");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception err) {
            throw new AuthenticationException("Authentication failed: " + err.getMessage());
        }

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthenticationException("User not found"));

        var jwtToken = jwtService.generateToken(user);

        revokeAllUserTokens(user);
        deleteALlUserTokens(user);
        saveUserToken(user, jwtToken);

        return new AuthResponse(jwtToken);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUserId(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void deleteALlUserTokens(User user) {
        var allTokens = tokenRepository.findAllTokensByUserId(user.getId());
        if (allTokens.isEmpty())
            return;
        tokenRepository.deleteAll();
    }

    public String removeBearer(String token) {
        if (token.startsWith("Bearer ")) {
            return token.substring(7);
        } else {
            return token;
        }
    }

    public Optional<User> getUserDataWithToken(String token) {
        String userEmail = jwtService.extractUsername(removeBearer(token));
        return userRepository.findByEmail(userEmail);
    }


}
