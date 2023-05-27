package com.user_registration.token;

import com.user_registration.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    public Boolean isTokenValid(String token) {
        Optional<Token> tokenOptional = tokenRepository.findByToken(removeBearerFromToken(token));
        return tokenOptional.isPresent() && !tokenOptional.get().isRevoked() && !tokenOptional.get().isExpired();
    }

    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUserId(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void deleteALlUserTokens(User user) {
        var allTokens = tokenRepository.findAllTokensByUserId(user.getId());
        if (allTokens.isEmpty())
            return;
        tokenRepository.deleteAll();
    }

    public String removeBearerFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            return token.substring(7);
        } else {
            return token;
        }
    }

    public Optional<Token> findTokenWithTokenString(String tokenString) {
        return tokenRepository.findByToken(removeBearerFromToken(tokenString));
    }

    public void deleteTokenByStringToken(String token) {
        tokenRepository.deleteTokenByToken(token);
    }
}
