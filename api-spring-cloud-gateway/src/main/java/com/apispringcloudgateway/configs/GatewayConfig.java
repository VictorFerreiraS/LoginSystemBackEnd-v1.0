package com.apispringcloudgateway.configs;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
    @EnableDiscoveryClient
    public class GatewayConfig {

        @Bean
        public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
            return builder.routes()
                    .route("email-ms", r -> r.path("/email-ms/**")
                            .uri("lb://email-ms"))
                    .route("regs-auth-ms", r -> r.path("/regs-auth-ms/**")
                            .uri("lb://regs-auth-ms"))
                    .build();
        }

    }

