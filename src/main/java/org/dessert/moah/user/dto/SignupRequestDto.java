package org.dessert.moah.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupRequestDto {
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private String address;
}
