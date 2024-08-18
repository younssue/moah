package org.dessert.moah.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderResponseList {
    List<OrderResponseDto> orderResponseDtoList;
}
