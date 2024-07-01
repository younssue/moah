package org.dessert.moah.item.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockDto {
    private Long stockId;
    private int sellAmount;
    private int stockAmount;

}
