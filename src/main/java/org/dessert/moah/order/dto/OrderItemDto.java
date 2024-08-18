package org.dessert.moah.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemDto {
    private Long orderItemId;
    private Long dessertId;
    private String dessertName;
    private int count;
    private int price;
}
