package org.dessert.moah.order.repository;

import org.dessert.moah.order.entity.Orders;
import org.dessert.moah.order.type.OrderStatus;

import java.util.List;

public interface OrderRepositoryCustom {
    List<Orders> findOrdersByUserId(Long userId, int page, int size);

    void updateOrderStatus(Long orderId, OrderStatus status);
}
