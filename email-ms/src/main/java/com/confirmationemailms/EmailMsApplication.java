package com.confirmationemailms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class EmailMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailMsApplication.class, args);
	}

}
