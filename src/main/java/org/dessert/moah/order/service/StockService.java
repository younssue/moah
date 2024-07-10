package org.dessert.moah.order.service;

import lombok.RequiredArgsConstructor;
import org.dessert.moah.item.entity.Stock;
import org.dessert.moah.item.repository.StockRepository;
import org.hibernate.StaleObjectStateException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;


    @Retryable(
            value = {ObjectOptimisticLockingFailureException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 100)
    )
    @Transactional
    public void decreaseStock(Long stockId, int amount) {
        //Stock stock = stockRepository.findByIdForUpdate(stockId).orElseThrow(() -> new RuntimeException("Stock not found"));
        Stock stock = stockRepository.findById(stockId).orElseThrow(() -> new RuntimeException("Stock not found"));
        stock.decreaseStock(amount);
        stockRepository.save(stock);

    }

}
