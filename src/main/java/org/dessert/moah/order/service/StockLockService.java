package org.dessert.moah.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dessert.moah.common.exception.NotFoundException;
import org.dessert.moah.common.type.ErrorCode;
import org.dessert.moah.item.entity.DessertItem;
import org.dessert.moah.item.entity.Stock;
import org.dessert.moah.item.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockLockService {
    private final StockRepository stockRepository;

/*
    @Transactional
    public void decreaseStock(Stock stock, int amount, DessertItem dessertItem) {

        log.info("재고 감소 시도: stockId={}, 감소량={}", stock.getId(), amount);
        stock.decreaseStock(amount, dessertItem);
        stockRepository.saveAndFlush(stock);

//        log.info("재고 감소 완료: stockId={}, 감소량={}, 남은 재고={}", stock.getId(), amount, stock.getStockAmount());
    }*/





    @Transactional
    public Stock getStock(Long stockId) {
        // 낙관적락 적용
        Stock stock = stockRepository.findByStockIdWithPessimisticLock(stockId)
                                     .orElseThrow(() -> new NotFoundException(ErrorCode.OUT_OF_STOCK));

        // 비관적락 적용
/*        Stock stock = stockRepository.findByStockIdWithPessimisticLock(stockId)
                                     .orElseThrow(() -> new NotFoundException(ErrorCode.OUT_OF_STOCK));*/
        return stock;
    }

    @Transactional(propagation = Propagation.REQUIRED) // 이 메소드 호출 시 상위 트랜잭션을 이어 받음
    public Stock decreaseStock(Long stockId, int amount, DessertItem dessertItem) {
        // 비관적 락을 사용하여 재고를 가져옴
        Stock stock = stockRepository.findByStockIdWithPessimisticLock(stockId)
                                     .orElseThrow(() -> new NotFoundException(ErrorCode.OUT_OF_STOCK));

        stock.decreaseStock(amount, dessertItem);


        return stockRepository.saveAndFlush(stock);
    }

    @Transactional // 10초 타임아웃 설정
    public Stock getStockWithPessimisticLock(Long stockId) {
        return stockRepository.findByStockIdWithPessimisticLock(stockId)
                              .orElseThrow(() -> new NotFoundException(ErrorCode.OUT_OF_STOCK));

    }


    public void saveStock(Stock stock, int amount, DessertItem dessertItem) {
        stock.decreaseStock(amount, dessertItem);
        stockRepository.saveAndFlush(stock);
    }
}
