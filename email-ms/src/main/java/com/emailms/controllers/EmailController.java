package com.emailms.controllers;
import com.emailms.dtos.EmailDto;
import com.emailms.email.EmailModel;
import com.emailms.services.EmailService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/email")
@AllArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("send-email")
    public ResponseEntity<String> sendEmail(@RequestBody @Valid EmailDto emailDto) {
        EmailModel emailModel = new EmailModel();
        BeanUtils.copyProperties(emailDto, emailModel);

        try {
            emailService.sendEmail(emailModel);
            return ResponseEntity.ok("email sent");
        } catch (Exception error) {
            String errorMessage = error.getMessage();
            if (errorMessage.isEmpty()) {
                return ResponseEntity.badRequest().body("Custom error message: No error message available");
            } else {
                return ResponseEntity .badRequest().body("Custom error message: " + errorMessage);
            }
        }
    }
}