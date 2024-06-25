package org.dessert.moah.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    /*
    주문완료, 배송중, 배송완료, 주문취소, 반품신청, 반품완료
    * */

    ORDER_COMPLETED,
    ORDER_CANCEL,
    BEING_DELIVERED,
    DELIVERY_COMPLETED,
    REQUEST_FOR_RETURN,
    RETURN_COMPLETED

}
