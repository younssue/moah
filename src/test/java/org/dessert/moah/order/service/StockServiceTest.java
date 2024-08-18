package org.dessert.moah.order.service;

import org.dessert.moah.item.entity.Stock;
import org.dessert.moah.item.repository.StockRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

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

        when(stockRepository.findByStockIdWithPessimisticLock(stockId)).thenReturn(Optional.of(new Stock()));

        // when & then
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            stockService.decreaseStock(stockId, amount);
        });

        verify(stockRepository, times(1)).findByStockIdWithPessimisticLock(stockId);
    }
}
