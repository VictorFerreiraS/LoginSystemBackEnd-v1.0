package com.user_registration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void registerUser(UserRegistrationRequest request) {
        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(request.password())
                .confirmed(false)
                .build();

        userRepository.save(user);
        // todo: check if email valid
        // todo: check if email not taken
        // todo: Send confirmation email
    }
}
