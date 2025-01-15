package com.chilly.security_svc.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MailConfig {

    @Value("${spring.mail.username}")
    private String emailUsername;

    private final static String PASSWORD_EMAIL_SUBJECT = "password recovery code";
    public final static String PASSWORD_RECOVERY_TEXT_TEMPLATE = "Your password recovery verification code is %s.";

    @Bean
    public SimpleMailMessage messageTemplate() {
        SimpleMailMessage template = new SimpleMailMessage();
        template.setFrom(emailUsername);
        template.setSubject(PASSWORD_EMAIL_SUBJECT);
        return template;
    }
}
