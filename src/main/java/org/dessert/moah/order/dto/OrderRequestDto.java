package org.dessert.moah.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderRequestDto {
    private Long dessertId;
    private int count;
}
