package org.dessert.moah.item.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.dessert.moah.item.entity.QStock;
import org.dessert.moah.item.entity.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.dessert.moah.item.entity.QStock.stock;

@Repository
@RequiredArgsConstructor
public class StockRepositoryImpl implements StockRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    /*@Override
    public Optional<Stock> findByStockIdWithLock(Long stockId) {
        QStock stock = QStock.stock;

        Stock result = queryFactory.selectFrom(stock)
                                   .where(stock.id.eq(stockId))
                                   .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                                   .fetchOne();

        return Optional.ofNullable(result);
    }*/

    @Override
    public Optional<Stock> findByStockIdWithPessimisticLock(Long stockId) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        try {
            Stock result = queryFactory.selectFrom(stock)
                                       .where(stock.id.eq(stockId))
                                       .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                                       .setHint("jakarta.persistence.lock.timeout", 5000) // 타임아웃을 5초로 설정
                                       .fetchOne();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            logger.error("stockId: " + stockId + " 실패", e);
            throw e;
        }
    }
}
