package org.dessert.moah.repository.order;

import org.aspectj.weaver.ast.Or;
import org.dessert.moah.entity.order.Orders;
import org.dessert.moah.entity.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long>,OrderRepositoryCustom {

    Optional<Orders> findByIdAndUsers(Long orderId, Users users);
}
