package org.dessert.moah.item.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.dessert.moah.common.type.ErrorCode;
import org.dessert.moah.common.exception.OutOfStockException;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Slf4j
@Table(name = "stock")
public class Stock /*implements Serializable*/ {

    private static final long serialVersionUID = 1L; // 명시적 serialVersionUID 설정


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Long id;

    @Column(nullable = false)
    private int stockAmount;

    @Column(nullable = false)
    private int sellAmount;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dessert_id")
    @JsonBackReference
    @JsonIgnore
    private DessertItem dessertItem;

   /* @Version
    private Long version;*/ // 낙관적 잠금을 위한 버전 필드



   @Builder
    public Stock(int stockAmount, int sellAmount, DessertItem dessertItem) {
        this.stockAmount = stockAmount;
        this.sellAmount = sellAmount;
        this.dessertItem = dessertItem;

    }

    public void decreaseStock(int amount , DessertItem dessertItem) {
        if (this.stockAmount < amount) {
            throw new OutOfStockException(ErrorCode.OUT_OF_STOCK);

        }
        this.stockAmount -= amount;
        this.sellAmount += amount;
        this.dessertItem = dessertItem;
        log.info("재고 감소: stockId={}, 감소량={}, 남은 재고={}", id, amount, stockAmount);
    }



    public void increaseStock(int amount) {
        this.stockAmount += amount;
        this.sellAmount -= amount;
    }
}
