package org.dessert.moah.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    EXAMPLE_SUCCESS("성공 예시 코드 입니다."),
    USER_SIGN_SUCCESS("회원가입 성공"),
    USER_UPDATE_INFO("회원 정보 수정 성공"),
    ORDER_CANCELLED("주문 취소 성공"),
    RETURN_SUCCESS("반품 성공"),
    STOCK_SUCCESS("재고 조회 성공");

    private final String description;
}
