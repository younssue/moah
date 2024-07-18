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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.dessert.moah.common.config.CacheConfig.CACHE1;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_PREFIX = "stock:";

    @Transactional
//    @Retryable(
//            value = {ConcurrencyFailureException.class, StaleObjectStateException.class},
//            maxAttempts = 5,
//            backoff = @Backoff(delay = 100)
//    )
    public void decreaseStock(Stock stock, int amount , DessertItem dessertItem) {
        /*Stock stock = stockRepository.findById(stockId)
                                     .orElseThrow(() -> new NotFoundException(ErrorCode.OUT_OF_STOCK));*/

        log.info("재고 감소 시도: stockId={}, 감소량={}", stock.getId(), amount);
        stock.decreaseStock(amount , dessertItem);
        //@Asn
        updateStockInCache(stock.getId(), stock);
        updateStockInDataBase(stock);
       /* redisTemplate.opsForValue()
                     .set("cache1::stock:" + stock.getId(), stock);*/

        log.info("재고 감소 완료: stockId={}, 감소량={}, 남은 재고={}", stock.getId(), amount, stock.getStockAmount());
    }

    private void updateStockInCache(Long stockId, Stock stock) {
        String cacheKey = CACHE_PREFIX + stockId;
        redisTemplate.opsForValue().set(cacheKey, stock);
    }

    @Async
    public void updateStockInDataBase(Stock stock){
        stockRepository.save(stock);

    }




/*
    @Cacheable(value = CACHE1, key = "'stock:' + #stock.id", unless = "#result == null")
    public Stock getStock(Stock stock) {
        log.info("Fetching stock from database for stockId={}", stock.getId());
        return stockRepository.findById(stock.getId())
                              .orElseThrow(() -> new NotFoundException(ErrorCode.OUT_OF_STOCK));

    }
*/

    public Stock getStock(Long stockId) {
        String cacheKey = CACHE_PREFIX + stockId;
        Stock stock = (Stock) redisTemplate.opsForValue().get(cacheKey);

        if (stock != null) {
            log.info("Redis에서  get을 해오는 상황 : stockId={}", stockId);
            return stock;
        }

        log.info("데이터 베이스에서 get을 해오는 상황 : stockId={}", stockId);
        stock = stockRepository.findById(stockId)
                               .orElseThrow(() -> new NotFoundException(ErrorCode.OUT_OF_STOCK));
        redisTemplate.opsForValue().set(cacheKey, stock);
        return stock;
    }


}
