package org.dessert.moah.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateUserInfoRequestDto {

    private String phoneNumber;
    private String address;
}
