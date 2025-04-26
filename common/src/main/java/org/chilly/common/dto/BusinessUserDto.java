package org.chilly.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessUserDto {
    private String email;
    private String phoneNumber;
    private String companyName;
    private String legalAddress;
    private String inn;
    private List<BusinessCategoryDto> businessCategories;
    private String kpp;
}
