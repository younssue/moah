package org.dessert.moah.order.service;

import org.dessert.moah.item.entity.Stock;
import org.dessert.moah.item.repository.StockRepository;
import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;

    @Test
    void testDecreaseStock_StaleObjectStateException() {
        // given
        Long stockId = 1L;
        int amount = 1;

        when(stockRepository.findByStockIdWithLock(stockId)).thenReturn(Optional.of(new Stock()));

        // when & then
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            stockService.decreaseStock(stockId, amount);
        });

        verify(stockRepository, times(1)).findByStockIdWithLock(stockId);
    }
}
