package com.springgatewayms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SpringGatewayMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringGatewayMsApplication.class, args);
    }
}