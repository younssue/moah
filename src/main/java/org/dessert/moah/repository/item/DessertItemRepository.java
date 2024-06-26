package org.dessert.moah.repository.item;

import org.dessert.moah.entity.Item.DessertItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DessertItemRepository extends JpaRepository<DessertItem,Long> {
    List<DessertItem> findByDeletedAtIsNull();

    DessertItem findByIdAndDeletedAtIsNull(Long dessertId);
}
