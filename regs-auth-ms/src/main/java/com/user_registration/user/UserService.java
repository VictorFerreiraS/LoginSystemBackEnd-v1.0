package com.user_registration.user;


import com.user_registration.jwt.JwtService;
import com.user_registration.exceptions.GettingTokenException;
import com.user_registration.exceptions.GettingUserException;
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

    public void deleteUserByToken(String tokenString) throws GettingUserException, GettingTokenException {
        User user = getUserDataWithToken(tokenString).orElseThrow(() -> new GettingUserException("User could not be get;"));
        Token token = tokenService.getTokenWithTokenString(tokenString).orElseThrow(() -> new GettingTokenException("Token could not be get;"));
        tokenService.deleteTokenByTokenId(token.getId());
        userRepository.deleteById(user.getId());
    }
}

