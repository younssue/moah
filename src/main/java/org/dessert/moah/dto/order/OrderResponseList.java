package org.dessert.moah.dto.order;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderResponseList {
    List<OrderResponseDto> orderResponseDtoList;
}
