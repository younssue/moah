package org.dessert.moah.item.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.dessert.moah.item.dto.DessertDto;
import org.dessert.moah.item.entity.DessertItem;
import org.dessert.moah.item.entity.QDessertItem;
import org.dessert.moah.item.entity.QStock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DessertItemRepositoryImpl implements DessertItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<DessertItem> findDessertItem(int page, int size) {
        //QOrders orders = QOrders.orders;
        QDessertItem dessertItem = QDessertItem.dessertItem;
        return queryFactory.selectFrom(dessertItem)
                           .where(dessertItem.deletedAt.isNull()) // deletedAt == null : 삭제되지 않았을 때
                           .offset(page * size)
                           .limit(size)
                           .fetch();


    }

    @Override
    public Optional<DessertDto> findDessertItemByPessimisticLock(Long dessertId) {
        QDessertItem qDessertItem = QDessertItem.dessertItem;
        QStock qstock = QStock.stock;


        DessertDto result = queryFactory.select(Projections.constructor(DessertDto.class,
                                                qDessertItem, qstock))
                                        .from(qDessertItem)
                                        .join(qDessertItem.stock, qstock).fetchJoin()
                                        .where(qDessertItem.id.eq(dessertId))
                                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                                        .fetchOne();

        return Optional.ofNullable(result);


    }

    @Override
    public Optional<DessertDto> findDessertItemByOptimisticLock(Long dessertId) {
        QDessertItem qDessertItem = QDessertItem.dessertItem;
        QStock qstock = QStock.stock;


        DessertDto result = queryFactory.select(Projections.constructor(DessertDto.class,
                                                qDessertItem, qstock))
                                        .from(qDessertItem)
                                        .join(qDessertItem.stock, qstock).fetchJoin()
                                        .where(qDessertItem.id.eq(dessertId))
                                        .setLockMode(LockModeType.OPTIMISTIC)
                                        .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<DessertDto> findDessertItem(Long dessertId) {
        QDessertItem qDessertItem = QDessertItem.dessertItem;
        QStock qstock = QStock.stock;


        DessertDto result = queryFactory.select(Projections.constructor(DessertDto.class,
                                                qDessertItem, qstock))
                                        .from(qDessertItem)
                                        .join(qDessertItem.stock, qstock).fetchJoin()
                                        .where(qDessertItem.id.eq(dessertId))
                                        .fetchOne();

        return Optional.ofNullable(result);
    }
}
