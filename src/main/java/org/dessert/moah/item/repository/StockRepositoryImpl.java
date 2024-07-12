package org.dessert.moah.item.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.dessert.moah.item.entity.QStock;
import org.dessert.moah.item.entity.Stock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StockRepositoryImpl implements StockRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Stock> findByStockIdWithLock(Long stockId) {
        QStock stock = QStock.stock;

        Stock result = queryFactory.selectFrom(stock)
                                   .where(stock.id.eq(stockId))
                                   .setLockMode(LockModeType.OPTIMISTIC)
                                   .fetchOne();

        return Optional.ofNullable(result);
    }
}
