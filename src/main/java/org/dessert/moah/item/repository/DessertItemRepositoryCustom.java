package org.dessert.moah.item.repository;

import org.dessert.moah.item.dto.DessertDto;
import org.dessert.moah.item.entity.DessertItem;

import java.util.List;
import java.util.Optional;

public interface DessertItemRepositoryCustom {
    List<DessertItem> findDessertItem( int page, int size);
    Optional<DessertDto> findDessertItemByPessimisticLock(Long dessertId);

    Optional<DessertDto> findDessertItemByOptimisticLock(Long dessertId);

    Optional<DessertDto> findDessertItem(Long dessertId);
}
