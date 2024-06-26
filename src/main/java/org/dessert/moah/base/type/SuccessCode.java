package org.dessert.moah.base.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    EXAMPLE_SUCCESS("성공 예시 코드 입니다."),
    USER_SIGN_SUCCESS("회원가입 성공"),
    USER_UPDATE_INFO("회원 정보 수정 성공");

    private final String description;
}