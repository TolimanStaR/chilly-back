package com.chilly.main_svc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginInfoChangeRequest {
    private Long id;
    private String email;
    private String phone;
}
