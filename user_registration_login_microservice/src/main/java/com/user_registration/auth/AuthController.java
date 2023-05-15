package com.user_registration.auth;

import com.user_registration.auth.requests.AuthenticationRequest;
import com.user_registration.auth.requests.RegisterRequest;
import com.user_registration.auth.responses.AuthResponse;
import com.user_registration.user.User;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    //   HTTP request to insert user into database
    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    //    HTTP REQUEST TO VALIDATE REQUEST WITH USERS IN DATABASE
    @CrossOrigin
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) throws AuthenticationException {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @CrossOrigin
    @GetMapping("/user")
    public ResponseEntity<User> getUser(
            @RequestHeader("Authorization") String token
    ) {
        System.out.println("getting...");
        System.out.println(token);
        // Verify the token to ensure the user is authenticated
        if (token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).build();
        }

        // Retrieve the user data using the token
        Optional<User> userOptional = service.getUserData(token);

        User user = userOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Return the user data in the response
        return ResponseEntity.ok(user);
    }

}
