package org.dessert.moah.base.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    EXAMPLE_FAIL("에러 예시 코드 입니다."),
    USER_NOT_FOUND("유저 정보를 찾을 수 없습니다");

    private final String description;
}
