package org.dessert.moah.item.repository;

import jakarta.persistence.LockModeType;
import org.dessert.moah.item.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock,Long> {

/*    @Lock(LockModeType.OPTIMISTIC)
    Optional<Stock> findByIdForUpdate(Long stockId);*/
}
