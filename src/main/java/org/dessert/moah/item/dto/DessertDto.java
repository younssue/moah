package org.dessert.moah.item.dto;

import org.dessert.moah.item.entity.DessertItem;
import org.dessert.moah.item.entity.Stock;

public record DessertDto(DessertItem dessertItem, Stock stock) {
}
