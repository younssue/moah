package org.dessert.moah.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StockDto {
    private Long stockId;
    private int sellAmount;
    private int stockAmount;

    @Builder
    public StockDto(Long stockId, int sellAmount, int stockAmount) {
        this.stockId = stockId;
        this.sellAmount = sellAmount;
        this.stockAmount = stockAmount;
    }
}
