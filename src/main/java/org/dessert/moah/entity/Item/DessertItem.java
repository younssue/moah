package org.dessert.moah.entity.Item;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dessert.moah.entity.common.BaseTime;
import org.dessert.moah.entity.type.DessertType;
import org.dessert.moah.entity.type.SaleStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "dessert_item")
public class DessertItem extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dessert_id")
    private Long id;

    @Column(nullable = false, name = "dessert_name")
    private String dessertName;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private int price;

    @Enumerated(value = EnumType.STRING)
    private SaleStatus saleStatus;

    @Enumerated(value = EnumType.STRING)
    private DessertType dessertType;

    @OneToOne(mappedBy = "dessertItem")
    private Stock stock;

    @OneToMany(mappedBy = "dessertItem")
    private List<DessertItemImage> dessertItemImages = new ArrayList<>();

    @Builder
    public DessertItem(String dessertName, String contents, int price, SaleStatus saleStatus, DessertType dessertType, Stock stock) {
        this.dessertName = dessertName;
        this.contents = contents;
        this.price = price;
        this.saleStatus = saleStatus;
        this.dessertType = dessertType;
        this.stock = stock;
    }
}
