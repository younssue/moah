package org.dessert.moah.repository.Item;

import org.dessert.moah.entity.Item.DessertItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DessertItemImageRepository extends JpaRepository<DessertItemImage,Long> {
}
