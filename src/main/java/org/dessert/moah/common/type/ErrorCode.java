package org.dessert.moah.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    EXAMPLE_FAIL("에러 예시 코드 입니다."),
    // 회원
    USER_NOT_FOUND("유저 정보를 찾을 수 없습니다"),
    // 상품
    ITEM_NOT_FOUND("상품 정보를 찾을 수 없습니다"),
    // 재고
    OUT_OF_STOCK("재고가 부족합니다"),

    // 주문
    ORDER_NOT_FOUND("주문을 찾을 수 없습니다"),
    INVALID_ORDER_STATUS("주문 취소를 할 수 없습니다"),
    INVALID_RETURN_STATUS("배송된 주문만 반품이 가능합니다"),
    RETURN_PERIOD_EXPIRED("반품기간이 만료되었습니다");

    private final String description;
}
