package com.user_registration.auth;

import com.user_registration.auth.requests.AuthenticationRequest;
import com.user_registration.auth.requests.RegisterRequest;
import com.user_registration.auth.responses.AuthResponse;
import com.user_registration.exceptions.UserAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    ) {
        try {

            return ResponseEntity.ok(authService.register(request));
        } catch (Throwable error) {
            AuthResponse exception = AuthResponse.builder().error(new UserAuthenticationException(error.getMessage(), error).getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
        }

//   SEND CONFIRMATION EMAIL


    }

    //    HTTP REQUEST TO VALIDATE REQUEST WITH USERS IN DATABASE
    @CrossOrigin
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        try {
            return ResponseEntity.ok(authService.authenticate(request));
        } catch (Throwable error) {
            AuthResponse exception = AuthResponse.builder().error(new UserAuthenticationException(error.getMessage(), error).getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
        }
    }

}
