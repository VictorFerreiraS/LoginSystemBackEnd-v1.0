package com.user_registration;

import com.user_registration.user.Role;
import com.user_registration.user.User;
import com.user_registration.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class RegistrationAuthenticationApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegistrationAuthenticationApplication.class, args);
    }

    @Bean
    public CommandLineRunner initialize(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            var user = User.builder().firstName("Victor").lastName("Ferreira").email("victor.fagundes586@gmail.com").password(passwordEncoder.encode("password")).role(Role.USER).confirmed(false).build();
            userRepository.save(user);
        };
    }
}
