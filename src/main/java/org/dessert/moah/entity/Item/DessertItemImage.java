package org.dessert.moah.entity.Item;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "dessert_item_image")
public class DessertItemImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "img_id")
    private Long id;

    private String img_url;

    @ManyToOne
    @JoinColumn(name = "dessert_id")
    private DessertItem dessertItem;

    @Builder
    public DessertItemImage(String img_url, DessertItem dessertItem) {
        this.img_url = img_url;
        this.dessertItem = dessertItem;
    }
}
