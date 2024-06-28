package org.dessert.moah.repository.order;

import org.dessert.moah.entity.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long>,OrderRepositoryCustom {
}
