package com.confirmationemailms.repositories;

import com.confirmationemailms.email.EmailModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<EmailModel, Long> {

}
