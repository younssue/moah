package org.dessert.moah.dto.item;

import lombok.Builder;
import lombok.Getter;
import org.dessert.moah.entity.type.DessertType;
import org.dessert.moah.entity.type.SaleStatus;

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

