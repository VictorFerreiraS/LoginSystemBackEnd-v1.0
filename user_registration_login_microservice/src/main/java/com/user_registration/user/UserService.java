package com.user_registration.user;


import com.user_registration.config.JwtService;
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
        if (tokenService.isTokenValid(tokenString) && getUserDataWithToken(tokenString).isPresent()) {
            System.out.println("741,96325807410852369,8520174369,3,85209614763,085214763,085291474963,25081763,25041841369,8520741369,85207");
            userRepository.deleteById(getUserDataWithToken(tokenString).get().getId());
        }
    }
}

