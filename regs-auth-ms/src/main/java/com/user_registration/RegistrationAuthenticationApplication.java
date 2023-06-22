package com.user_registration;

import com.user_registration.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@AllArgsConstructor
@EnableDiscoveryClient
@SpringBootApplication
public class RegistrationAuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistrationAuthenticationApplication.class, args);

    }
}
