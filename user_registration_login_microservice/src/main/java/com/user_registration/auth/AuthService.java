package com.user_registration.auth;

import com.user_registration.auth.requests.AuthenticationRequest;
import com.user_registration.auth.requests.RegisterRequest;
import com.user_registration.auth.responses.AuthResponse;
import com.user_registration.config.JwtService;
import com.user_registration.user.Role;
import com.user_registration.user.User;
import com.user_registration.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
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
    private final UserRepository repository;

    public AuthResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .confirmed(false)
                .role(Role.USER)
                .build();


        if (repository.findByEmail(user.getEmail()).isEmpty()) {
            repository.save(user);
        } else {
            throw new IllegalArgumentException("Email Taken");
        }

        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwtToken).build();
    }

    public AuthResponse authenticate(AuthenticationRequest request) throws AuthenticationException {
        System.out.println("Authenticating...");
        System.out.println(request.getEmail());
        System.out.println(request.getPassword());


//       todo: BAD CREDENTIALS Exception;
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception err) {
            throw new AuthenticationException(err.getMessage());
        }


        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("user not found"));

        System.out.println(user);

        var jwtToken = jwtService.generateToken(user);

        System.out.println(jwtToken);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}
