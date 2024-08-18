package org.dessert.moah.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dessert.moah.item.type.DessertType;
import org.dessert.moah.item.type.SaleStatus;

@Getter
@NoArgsConstructor
public class ItemRequestDto2 {

    private String dessertName;
    private String contents;
    private int price;
    private SaleStatus saleStatus;
    private DessertType dessertType;
    private StockDto stock;

    @Builder
    public ItemRequestDto2(String dessertName, String contents, int price, SaleStatus saleStatus, DessertType dessertType, StockDto stock) {
        this.dessertName = dessertName;
        this.contents = contents;
        this.price = price;
        this.saleStatus = saleStatus;
        this.dessertType = dessertType;
        this.stock = stock;
    }
}

