package org.dessert.moah.item.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dessert.moah.common.type.ErrorCode;
import org.dessert.moah.common.exception.OutOfStockException;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Long id;

    @Column(nullable = false)
    private int stockAmount;

    @Column(nullable = false)
    private int sellAmount;

    @OneToOne
    @JoinColumn(name = "dessert_id")
    private DessertItem dessertItem;

    @Version
    private Long version; // 낙관적 잠금을 위한 버전 필드



   @Builder
    public Stock(int stockAmount, int sellAmount, DessertItem dessertItem,Long version) {
        this.stockAmount = stockAmount;
        this.sellAmount = sellAmount;
        this.dessertItem = dessertItem;
        this.version = 0L; // 초기 버전 값 설정
    }

    public void decreaseStock(int amount) {
        if (this.stockAmount < amount) {
            throw new OutOfStockException(ErrorCode.OUT_OF_STOCK);

        }
        this.stockAmount -= amount;
        this.sellAmount += amount;
    }

    public void increaseStock(int amount) {
        this.stockAmount += amount;
        this.sellAmount -= amount;
    }
}
