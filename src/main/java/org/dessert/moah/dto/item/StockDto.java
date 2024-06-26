package org.dessert.moah.dto.item;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockDto {
    private Long stockId;
    private int sellAmount;
    private int stockAmount;

}
