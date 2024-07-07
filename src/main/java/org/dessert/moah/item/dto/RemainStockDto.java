package org.dessert.moah.item.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RemainStockDto {
    private Long dessertId;
    private String dessertName;
    private int stockAmount;
}
