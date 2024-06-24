package org.dessert.moah.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "dessert_item")
public class Stock {

    /*CREATE TABLE `stock`(
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `stock_amount` INT NOT NULL,
    `sell_amount` INT NOT NULL,
    `dessert_item_id` INT NOT NULL
);*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int stockAmount;
    private int sellAmount;

}
