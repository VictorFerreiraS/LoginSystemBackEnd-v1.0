package com.user_registration.user;


import com.user_registration.config.JwtService;
import com.user_registration.token.Token;
import com.user_registration.token.TokenRepository;
import com.user_registration.token.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;
    private final JwtService jwtService;

    public Optional<User> getUserDataWithToken(String token) {
        String userEmail = jwtService.extractUsername(tokenService.removeBearerFromToken(token));
        return userRepository.findByEmail(userEmail);
    }

    public void deleteUserByToken(String tokenString) {
        if (!tokenService.isTokenValid(tokenString))
            return;
        Optional<Token> token = tokenRepository.findTokenByToken(tokenString);
        if (token.isPresent()) {
            User user = token.get().getUser();
            userRepository.deleteById(user.getId());
        }
    }
}
