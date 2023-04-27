package com.user_registration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailValidator emailValidator;

    public void registerUser(UserRegistrationRequest request) {
        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(request.password())
                .confirmed(false)
                .build();

        if (userRepository.findByEmail(user.getEmail()) == null) {
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Email Taken");
        }

    }

    // todo: check if email valid
    // todo: Send confirmation email

}
