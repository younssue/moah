package org.dessert.moah.item.dto;

import org.dessert.moah.item.entity.DessertItem;
import org.dessert.moah.item.entity.DessertItemImage;
import org.dessert.moah.item.entity.Stock;

public record ItemRequestDto(DessertItem dessertItem, Stock stock , DessertItemImage dessertItemImage) {
}
