package org.dessert.moah.item.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.dessert.moah.item.entity.DessertItem;
import org.dessert.moah.item.entity.QDessertItem;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
