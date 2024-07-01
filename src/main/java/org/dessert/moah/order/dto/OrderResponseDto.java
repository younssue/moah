package org.dessert.moah.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderResponseDto {
    private Long orderId;
    private String orderStatus;
    private LocalDateTime orderDate;
    private int totalPrice;
    private List<OrderItemDto> orderItemDtoList;

}