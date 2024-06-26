package org.dessert.moah.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequestDto {

    private String password;
    private String email;

}
