package org.dessert.moah.item.repository;

import org.dessert.moah.item.entity.DessertItem;
import org.dessert.moah.item.entity.DessertItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DessertItemImageRepository extends JpaRepository<DessertItemImage,Long> {
    DessertItemImage findByDessertItem(DessertItem savedDessertItem);
}
