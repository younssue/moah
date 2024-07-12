package org.dessert.moah.order.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dessert.moah.order.type.OrderStatus;
import org.dessert.moah.user.entity.Users;
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


    @Column(name = "order_status", nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    @Column(name = "order_date")
    private LocalDateTime orderDate; // 주문 시간

    @Column(name = "delivery_address")
    private String deliveryAddress;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @OneToMany(mappedBy = "orders", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    public Orders(OrderStatus orderStatus, LocalDateTime orderDate, Users users, List<OrderItem> orderItems, String deliveryAddress) {
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.users = users;
        this.orderItems = new ArrayList<>();
        this.deliveryAddress = deliveryAddress;
    }

    // 전체 품목의 주문 금액
    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrders(this); // 양방향 연관관계 설정
    }
}
