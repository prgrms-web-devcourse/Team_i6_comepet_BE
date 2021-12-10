package com.pet.infra;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;

@Slf4j
@Profile("local")
public class ConsoleEmailSender implements MailSender {

    @Override
    public void send(EmailMessage emailMessage) {
        log.info(" to : {}, subject : {}, message : {}",
            emailMessage.getTo(),
            emailMessage.getSubject(),
            emailMessage.getMessage()
        );
    }

}