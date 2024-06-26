package org.dessert.moah.dto.user;

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
