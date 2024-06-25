package org.dessert.moah.entity.order;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dessert.moah.entity.type.OrderStatus;
import org.dessert.moah.entity.user.Users;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;


    @Column(name = "order_status")
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    @Column(name = "order_date")
    private LocalDateTime orderDate; // 주문 시간


    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @OneToMany(mappedBy = "orders")
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    public Orders(OrderStatus orderStatus, LocalDateTime orderDate, Users users) {
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.users = users;
    }
}
