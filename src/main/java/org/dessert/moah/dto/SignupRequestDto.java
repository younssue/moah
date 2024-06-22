package org.dessert.moah.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupRequestDto {
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
//    private boolean admin = false;
//    private String adminToken = "";
}
