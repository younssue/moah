package org.dessert.moah.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dessert.moah.entity.common.BaseTime;
import org.dessert.moah.entity.type.DessertType;
import org.dessert.moah.entity.type.SaleStatus;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "dessert_item")
public class DessertItem extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    * CREATE TABLE `dessert_item`(
    `id` INT NOT NULL,
    `type` VARCHAR(255) NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `contents` VARCHAR(255) NOT NULL,
    `price` INT NOT NULL,
    `sale_status` ENUM('') NOT NULL,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NOT NULL,
    `deleted_at` DATETIME NOT NULL,
    PRIMARY KEY(`id`)
);
    * */

    private String dessertName;
    private String contents;
    private int price;
    private SaleStatus saleStatus;
    private DessertType dessertType;
}
