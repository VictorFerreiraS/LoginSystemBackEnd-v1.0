package com.user_registration.user;

import com.user_registration.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final TokenService tokenService;
    private final UserService userService;

    @CrossOrigin
    @GetMapping("get-user")
    public ResponseEntity<User> getUser(
            @RequestHeader("Authorization") String token
    ) {
        // Retrieve the user data using the token
        Optional<User> user = userService.getUserDataWithToken(token);
        // Verify the token to ensure the user is authenticated
        if (tokenService.isTokenValid(token) && user.isPresent()) {
            // Return the user data in the response
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).build();
        }
    }

    @GetMapping("delete-user")
    public ResponseEntity<String> deleteUser(
            @RequestHeader("Authorization") String token
    ) {
        if (tokenService.isTokenValid(token)) {
            try {
                userService.deleteUserByToken(token);
                return ResponseEntity.ok("User Deleted successfully!");
            } catch (Throwable error) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).body("User token is not valid please sign in again");
        }
    }
}
