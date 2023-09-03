package com.user_registration.user;

import com.user_registration.exceptions.ChangePasswordException;
import com.user_registration.exceptions.GettingUserException;
import com.user_registration.requests.ChangePasswordRequest;
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

    @CrossOrigin
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

    @CrossOrigin
    @GetMapping("send-confirmation-email")
    public ResponseEntity<String> confirmUserEmail(@RequestHeader("Authorization") String token) {
        if (tokenService.isTokenValid(token)) {
            try {
                userService.sendConfirmationEmail(token);
                return ResponseEntity.ok("Email Sent");
            } catch (Throwable error) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).body("User token is not valid please sign in again");
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestHeader("Authorization") String token, @RequestBody ChangePasswordRequest passwordData) {
        if (tokenService.isTokenValid(token)) {
            String oldPassword = passwordData.getOldPassword();
            String newPassword = passwordData.getNewPassword();
            try {
                userService.changeUserPassword(token, oldPassword, newPassword);
                return ResponseEntity.ok("Password changed successfully");
            } catch (GettingUserException | ChangePasswordException error) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
            } catch (Exception exception) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + exception.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User token is not valid, please sign in again");
        }
    }


}
