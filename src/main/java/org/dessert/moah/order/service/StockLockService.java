package org.dessert.moah.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dessert.moah.common.exception.NotFoundException;
import org.dessert.moah.common.type.ErrorCode;
import org.dessert.moah.item.entity.DessertItem;
import org.dessert.moah.item.entity.Stock;
import org.dessert.moah.item.repository.StockRepository;
import org.hibernate.StaleObjectStateException;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockLockService {
    private final StockRepository stockRepository;
    private final RedisTemplate<String, Object> redisTemplate;


    @Transactional
    @Retryable(
            value = {ConcurrencyFailureException.class, StaleObjectStateException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 100)
    )
    public void decreaseStock(Stock stock, int amount , DessertItem dessertItem) {
/*        Stock stockForLock = stockRepository.findByStockIdWithPessimisticLock(stock.getId())
                                     .orElseThrow(() -> new NotFoundException(ErrorCode.OUT_OF_STOCK));*/

        Stock stockForLock = stockRepository.findByStockIdWithLock(stock.getId())
                                            .orElseThrow(() -> new NotFoundException(ErrorCode.OUT_OF_STOCK));
        log.info("재고 감소 시도: stockId={}, 감소량={}", stockForLock.getId(), amount);
        stockForLock.decreaseStock(amount , dessertItem);
        stockRepository.save(stockForLock);

        log.info("재고 감소 완료: stockId={}, 감소량={}, 남은 재고={}", stockForLock.getId(), amount, stockForLock.getStockAmount());
    }



}
