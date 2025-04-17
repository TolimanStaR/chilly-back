package org.chilly.common.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterBusinessUserRequest {
    private String email;
    private String phoneNumber;
    private String password;
    private String companyName;
    private String legalAddress;
    private String inn;
    private List<BusinessCategoryDto> businessCategories;
    private String kpp;
}
