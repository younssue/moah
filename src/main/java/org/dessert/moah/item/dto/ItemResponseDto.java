package org.dessert.moah.item.dto;

import lombok.Builder;
import lombok.Getter;
import org.dessert.moah.item.type.DessertType;
import org.dessert.moah.item.type.SaleStatus;

@Getter
@Builder
public class ItemResponseDto {
    private Long dessert_id;
    private String dessertName;
    private String contents;
    private int price;
    private SaleStatus saleStatus;
    private DessertType dessertType;
    private StockDto stock;
    private String dessertItemImg;
}

