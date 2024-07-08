package org.dessert.moah.item.repository;

import org.dessert.moah.item.entity.DessertItem;

import java.util.List;

public interface DessertItemRepositoryCustom {
    List<DessertItem> findDessertItem( int page, int size);
}
