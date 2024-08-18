package org.dessert.moah.order.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {


    ORDER_COMPLETED("주문 완료"),
    ORDER_CANCELLED("주문 취소"),
    BEING_DELIVERED("배송 중"),
    DELIVERY_COMPLETED("배송 완료"),
    REQUEST_FOR_RETURN("반품 신청"),
    RETURN_COMPLETED("반품 완료");

    private final String description;
}
