package com.chilly.security_svc.service;

import com.chilly.security_svc.config.MailConfig;
import com.chilly.security_svc.model.User;
import com.chilly.security_svc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chilly.common.dto.*;
import org.chilly.common.exception.CallFailedException;
import org.chilly.common.exception.NoSuchEntityException;
import org.chilly.common.exception.UnauthorizedAccessException;
import org.chilly.common.exception.WrongDataException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordService {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final MailSender mailSender;
    private final SimpleMailMessage messageTemplate;
    private final Random random = new Random();


    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchEntityException("no user with id = " + userId));

        if (!encoder.matches(request.getOldPassword(), user.getPassword()))  {
            throw new UnauthorizedAccessException("wrong password");
        }

        user.setPassword(encoder.encode(request.getNewPassword()));
    }


    public void sendRecoveryCode(RecoveryCodeSendRequest request) {
        User user = findUserByEmailOrException(request.getEmail());

        String code = String.valueOf(random.nextLong(100000, 1000000));
        try {
            SimpleMailMessage message = new SimpleMailMessage(messageTemplate);
            message.setTo(user.getEmail());
            message.setText(String.format(MailConfig.PASSWORD_RECOVERY_TEXT_TEMPLATE, code));
            mailSender.send(message);
        } catch (Exception e) {
            throw new CallFailedException("unable to send email to " + user.getEmail());
        }
        user.setRecoveryCode(code);
    }

    public CodeVerificationResponse verifyCode(CodeVerificationRequest request) {
        User user = findUserByEmailOrException(request.getEmail());
        Boolean verified = Objects.equals(user.getRecoveryCode(), request.getCode());
        return new CodeVerificationResponse(verified);
    }

    public void recoverPassword(PasswordRecoveryRequest request) {
        User user = findUserByEmailOrException(request.getEmail());
        if (!Objects.equals(user.getRecoveryCode(), request.getCode())) {
            throw new WrongDataException("verification code doesn't match");
        }

        user.setPassword(encoder.encode(request.getNewPassword()));
        user.setRecoveryCode(null);
    }

    private User findUserByEmailOrException(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchEntityException("no user with email = " + email));
    }

}
