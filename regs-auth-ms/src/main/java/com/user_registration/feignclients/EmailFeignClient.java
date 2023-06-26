package com.user_registration.feignclients;

import com.user_registration.auth.dtos.EmailDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "email-ms", url = "localhost:8081", path = "/api/v1/email")
public interface EmailFeignClient {

    @PostMapping("send-email")
    ResponseEntity<String> sendEmail(@RequestBody @Valid EmailDto emailDto);
}
