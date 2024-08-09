package com.chilly.security_svc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String phoneNumber;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
}

