package org.dessert.moah.entity.Item;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder
    public Stock(int stockAmount, int sellAmount, DessertItem dessertItem) {
        this.stockAmount = stockAmount;
        this.sellAmount = sellAmount;
        this.dessertItem = dessertItem;
    }
}
