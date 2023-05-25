package com.user_registration.auth;

import com.user_registration.auth.requests.AuthenticationRequest;
import com.user_registration.auth.requests.RegisterRequest;
import com.user_registration.auth.responses.AuthResponse;
import com.user_registration.token.TokenService;
import com.user_registration.user.User;
import com.user_registration.user.UserService;
import jakarta.security.auth.message.AuthException;
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

    private final AuthService authService;
    private final TokenService tokenService;
    private final UserService userService;

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

    @CrossOrigin
    @GetMapping("/get-user")
    public ResponseEntity<User> getUser(
            @RequestHeader("Authorization") String token
    ) {
        // Verify the token to ensure the user is authenticated
        if (tokenService.isTokenValid(token)) {
            // Retrieve the user data using the token
            Optional<User> userOptional = userService.getUserDataWithToken(token);
            User user = userOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            // Return the user data in the response
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).build();
        }
    }

    @GetMapping("delete-user")
    public ResponseEntity<String> deleteUser(
            @RequestHeader("Authorization") String token
    ) {
        if (tokenService.isTokenValid(token)) {
            userService.deleteUserByToken(token);
            return ResponseEntity.ok("User Deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).build();
        }
    }


}
