package org.dessert.moah.item.repository;

import org.dessert.moah.item.entity.Stock;

import java.util.Optional;

public interface StockRepositoryCustom {
    Optional<Stock> findByStockIdWithPessimisticLock(Long stockId);
}
