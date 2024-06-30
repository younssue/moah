package org.dessert.moah.repository.order;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.dessert.moah.entity.order.Orders;
import org.dessert.moah.entity.order.QOrders;
import org.dessert.moah.entity.type.OrderStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Orders> findOrdersByUserId(Long userId, int page, int size) {
        QOrders orders = QOrders.orders;
        return queryFactory.selectFrom(orders)
                           .where(orders.users.id.eq(userId))
                           .offset(page * size)
                           .limit(size)
                           .fetch();
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        QOrders orders = QOrders.orders;
        queryFactory.update(orders)
                    .set(orders.orderStatus, status)
                    .where(orders.id.eq(orderId))
                    .execute();
    }
}
