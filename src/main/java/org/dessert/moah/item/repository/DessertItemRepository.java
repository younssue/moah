package org.dessert.moah.item.repository;

import jakarta.persistence.LockModeType;
import org.dessert.moah.item.entity.DessertItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DessertItemRepository extends JpaRepository<DessertItem,Long>, DessertItemRepositoryCustom {
    //List<DessertItem> findByDeletedAtIsNull();


    DessertItem findByIdAndDeletedAtIsNull(Long dessertId);
}
