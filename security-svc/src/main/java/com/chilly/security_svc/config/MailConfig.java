package com.chilly.security_svc.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MailConfig {

    @Value("${spring.mail.username}")
    private String emailUsername;

    @Value("${spring.mail.password}")
    private String emailPassword;

    private final static String PASSWORD_EMAIL_SUBJECT = "password recovery code";
    public final static String PASSWORD_RECOVERY_TEXT_TEMPLATE = "Your password recovery verification code is %s.";

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(emailUsername);
        mailSender.setPassword(emailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Bean
    public SimpleMailMessage messageTemplate() {
//        JavaMailSenderImpl impl = (JavaMailSenderImpl) sender;
//        log.info("props: {}, {}, {}, {}", impl.getUsername(), impl.getPassword(), impl.getHost(), impl.getPort());
        SimpleMailMessage template = new SimpleMailMessage();
        template.setFrom(emailUsername);
        template.setSubject(PASSWORD_EMAIL_SUBJECT);
        return template;
    }
}
