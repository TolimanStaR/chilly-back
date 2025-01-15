package com.chilly.security_svc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordRecoveryRequest {
    private String email;
    private String code;
    private String newPassword;
}
