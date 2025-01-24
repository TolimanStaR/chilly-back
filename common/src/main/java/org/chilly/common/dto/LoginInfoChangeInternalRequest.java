package org.chilly.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginInfoChangeInternalRequest {
    private Long id;
    private String email;
    private String phone;
}
