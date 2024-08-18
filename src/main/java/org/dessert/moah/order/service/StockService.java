package org.dessert.moah.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dessert.moah.common.exception.NotFoundException;
import org.dessert.moah.common.type.ErrorCode;
import org.dessert.moah.item.entity.DessertItem;
import org.dessert.moah.item.entity.Stock;
import org.dessert.moah.item.repository.StockRepository;
import org.hibernate.StaleObjectStateException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.data.repository.query.Param;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.dessert.moah.common.config.CacheConfig.CACHE1;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;


    @Transactional
    public void decreaseStock(Stock stock, int amount, DessertItem dessertItem) {

        log.info("재고 감소 시도: stockId={}, 감소량={}", stock.getId(), amount);
        stock.decreaseStock(amount, dessertItem);
         stockRepository.saveAndFlush(stock);

        log.info("재고 감소 완료: stockId={}, 감소량={}, 남은 재고={}", stock.getId(), amount, stock.getStockAmount());
    }


    @Transactional
    public Stock getStock(Long stockId) {
        Stock stock = stockRepository.findById(stockId)
                                     .orElseThrow(() -> new NotFoundException(ErrorCode.OUT_OF_STOCK));
        return stock;
    }


}
