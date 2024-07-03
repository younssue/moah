package org.dessert.moah.order.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dessert.moah.item.entity.DessertItem;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @Column(nullable = false, name = "order_price")
    private int orderPrice; // 아이템 가격

    @Column(nullable = false)
    private int count; //수량

    @Setter
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "dessert_id")
    private DessertItem dessertItem;

    @Builder
    public OrderItem(int orderPrice, int count, Orders orders, DessertItem dessertItem) {
        this.orderPrice = orderPrice;
        this.count = count;
        this.orders = orders;
        this.dessertItem = dessertItem;
    }

    // 주문 가격 * 주문 수량 = 해당 상품을 주문한 총 가격
    public int getTotalPrice(){
        return orderPrice*count;
    }

}
