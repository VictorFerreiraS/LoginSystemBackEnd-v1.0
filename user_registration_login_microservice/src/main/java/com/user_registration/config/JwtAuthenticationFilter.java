package com.user_registration.config;

import com.user_registration.token.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

/*
    The method performs the following tasks:
    * Extracts the Authorization header from the incoming HTTP request
    * Validates the JWT token from the header
    * Extracts the user email from the JWT token
    * Loads the user details from the user details service
    * Creates an Authentication object from the user details and sets it in the SecurityContextHolder if the JWT token is valid
    * Passes the request and response to the next filter in the filter chain
    * */

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
//      If it's not valid, pass the request and response to the next filter in the chain and return
            return;
        }

//        Extract the JWT token from the header
        jwt = authHeader.substring(7);
//        Extract the user email from the JWT token using a jwtService instance
        String userEmail = jwtService.extractUsername(jwt);

//        Check if the user email is not null and if there is no existing authentication in the SecurityContextHolder
//        If there is an existing authentication, it means the user is already authenticated, so pass
//        the request and response to the next filter in the chain and return
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//        If there is no existing authentication, load the user details from the user details service using the user email
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
//         Check if is expired or revoked
            var isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
//         Check if the JWT token is valid using a jwtService instance, and tokenRepository to see if token is revoked or expired
            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
//                If the token is valid, create an UsernamePasswordAuthenticationToken instance using the user
//                details and set it in the SecurityContextHolder
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new
                        WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                throw new ResponseStatusException(HttpStatusCode.valueOf(403), "Invalid Token");
            }

        }
//      Pass the request and response to the next filter in the filter chain
        filterChain.doFilter(request, response);
    }
}
