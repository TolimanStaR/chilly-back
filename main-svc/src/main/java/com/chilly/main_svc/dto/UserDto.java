package com.chilly.main_svc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;
    private String phoneNumber;
    private String firstname;
    private String lastname;
    private String email;
}
