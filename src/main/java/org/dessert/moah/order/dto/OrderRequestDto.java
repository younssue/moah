package org.dessert.moah.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRequestDto {
    private Long dessertId;
    private int count;
    private String deliveryAddress;

    @Builder
    public OrderRequestDto(Long dessertId, int count, String deliveryAddress) {
        this.dessertId = dessertId;
        this.count = count;
        this.deliveryAddress = deliveryAddress;
    }
}
