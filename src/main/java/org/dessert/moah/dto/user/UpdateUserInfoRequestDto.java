package org.dessert.moah.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateUserInfoRequestDto {

    private String phoneNumber;
    private String address;
}
