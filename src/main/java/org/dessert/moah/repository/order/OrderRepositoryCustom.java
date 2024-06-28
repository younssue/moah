package org.dessert.moah.repository.order;

import org.dessert.moah.entity.order.Orders;
import org.dessert.moah.entity.type.OrderStatus;

import java.util.List;

public interface OrderRepositoryCustom {
    List<Orders> findOrdersByUserId(Long userId, int page, int size);

    void updateOrderStatus(Long orderId, OrderStatus status);
}
