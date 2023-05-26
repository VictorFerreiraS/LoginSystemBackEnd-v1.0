package com.user_registration.auth;

import com.user_registration.auth.requests.AuthenticationRequest;
import com.user_registration.auth.requests.RegisterRequest;
import com.user_registration.auth.responses.AuthResponse;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    //   HTTP request to insert user into database
    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request
    ) throws AuthException {
        return ResponseEntity.ok(authService.register(request));
    }

    //    HTTP REQUEST TO VALIDATE REQUEST WITH USERS IN DATABASE
    @CrossOrigin
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) throws AuthenticationException {
        return ResponseEntity.ok(authService.authenticate(request));
    }

}
