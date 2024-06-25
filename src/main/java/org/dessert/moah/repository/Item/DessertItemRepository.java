package org.dessert.moah.repository.Item;

import org.dessert.moah.entity.Item.DessertItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DessertItemRepository extends JpaRepository<DessertItem,Long> {
}
