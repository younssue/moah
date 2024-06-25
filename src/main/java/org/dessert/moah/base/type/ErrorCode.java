package org.dessert.moah.base.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    EXAMPLE_FAIL("에러 예시 코드 입니다.");

    private final String description;
}
