package com.user_registration.user;


import com.user_registration.config.JwtService;
import com.user_registration.token.Token;
import com.user_registration.token.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final JwtService jwtService;

    public Optional<User> getUserDataWithToken(String tokenString) {
        String userEmail = jwtService.extractUsername(tokenService.removeBearerFromToken(tokenString));
        return userRepository.findByEmail(userEmail);
    }

    public void deleteUserByToken(String tokenString) {
        if (!tokenService.isTokenValid(tokenString))
            return;
        Optional<Token> token = tokenService.findTokenWithTokenString(tokenString);
        if (token.isPresent()) {
            User user = token.get().getUser();
            userRepository.deleteById(user.getId());
        }
    }
}
