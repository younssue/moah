package org.dessert.moah.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private String accessToken;
}
