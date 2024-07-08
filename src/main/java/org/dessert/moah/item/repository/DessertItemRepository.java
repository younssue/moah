package org.dessert.moah.item.repository;

import org.dessert.moah.item.entity.DessertItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DessertItemRepository extends JpaRepository<DessertItem,Long>, DessertItemRepositoryCustom {
    //List<DessertItem> findByDeletedAtIsNull();

    DessertItem findByIdAndDeletedAtIsNull(Long dessertId);
}
