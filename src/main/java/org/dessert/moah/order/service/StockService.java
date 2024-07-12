package org.dessert.moah.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dessert.moah.item.entity.Stock;
import org.dessert.moah.item.repository.StockRepository;
import org.hibernate.StaleObjectStateException;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;

/*
    @Transactional
//    @Retryable(
//            value = {ObjectOptimisticLockingFailureException.class, StaleObjectStateException.class},
//            maxAttempts = 10,
//            backoff = @Backoff(delay = 100)
//    )
    public void decreaseStock(Long stockId, int amount) {

        Stock stock = stockRepository.findByStockIdWithLock(stockId)
                                     .orElseThrow(() -> new RuntimeException("Stock not found"));
        stock.decreaseStock(amount);

        //stock.setVersion(stock.getVersion() + 1);
        log.info("확인: {}", stock.getVersion());
        stockRepository.save(stock);

    }*/

    @Transactional
    @Retryable(
            value = {ConcurrencyFailureException.class, StaleObjectStateException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 100)
    )
    public void decreaseStock(Long stockId, int amount) {
        Stock stock = null;
        try {
            stock = stockRepository.findById(stockId)
                                   .orElseThrow(() -> new RuntimeException("Stock not found"));
            stock.decreaseStock(amount);
            log.info("version: {}", stock.getVersion());
            stockRepository.save(stock);
//            stock.setVersion(stock.getVersion() + 1);

        } /*catch (ConcurrencyFailureException | StaleObjectStateException e) {
            // Log the error with version info
            if (stock != null) {
                log.error("Optimistic Locking Failure on Stock with ID: {}, version: {}", stockId, stock.getVersion(), e);
            } else {
                log.error("Optimistic Locking Failure on Stock with ID: {}", stockId, e);
            }
            throw e;
        }*/  catch (Exception  e) {
            // Log the error with version info
            if (stock != null) {
                log.error("Optimistic Locking Failure on Stock with ID: {}, version: {}", stockId, stock.getVersion(), e);
            } else {
                log.error("Optimistic Locking Failure on Stock with ID: {}", stockId, e);
            }
            throw e;
        }
    }


}
