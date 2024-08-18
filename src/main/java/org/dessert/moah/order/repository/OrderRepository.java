package org.dessert.moah.order.repository;

import org.dessert.moah.order.entity.Orders;
import org.dessert.moah.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long>,OrderRepositoryCustom {

    Optional<Orders> findByIdAndUsers(Long orderId, Users users);
}
