package org.dessert.moah.order.facade;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dessert.moah.item.entity.DessertItem;
import org.dessert.moah.item.entity.Stock;
import org.dessert.moah.order.dto.OrderRequestDto;
import org.dessert.moah.order.service.OrderService;
import org.dessert.moah.order.service.StockService;
import org.dessert.moah.user.dto.CustomUserDetails;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedissonLockStockFacade {
    private final RedissonClient redissonClient;

    private final StockService stockService;
    private final OrderService orderService;

    public RedissonLockStockFacade(RedissonClient redissonClient, StockService stockService, OrderService orderService) {
        this.redissonClient = redissonClient;
        this.stockService = stockService;
        this.orderService = orderService;
    }

/*    public void decreaseWithOptimistic(CustomUserDetails customUserDetails, OrderRequestDto orderRequestDto) {
        int retry = 0;
        int maxTry = 3;
        while (true) {
            try {
                orderService.createOrder(customUserDetails, orderRequestDto);
            } catch (Exception e) {
                retry++;
                log.info("시도 횟수: {}", retry);
                if (retry > maxTry) {
                    log.error("최대 시도");
                    throw e;
                }
            }
        }
    }*/

    public void decrease(CustomUserDetails customUserDetails, OrderRequestDto orderRequestDto) {
        RLock lock = redissonClient.getLock(orderRequestDto.getDessertId().toString());

        try {
           // log.info("lock 시도: stockId={}, threadId={}", stock.getId(), Thread.currentThread().getId());
            boolean available = lock.tryLock(15, 1, TimeUnit.SECONDS);

//            if (!available) {
//                log.info("lock 획득 실패: stockId={}, threadId={}", stock.getId(), Thread.currentThread().getId());
//                return;
//            }

//            log.info("lock 획득 성공: stockId={}, threadId={}", stock.getId(), Thread.currentThread().getId());
//            log.info("재고 감소 시도 전: stockId={}, 감소량={}, 현재 재고={}, threadId={}", stock.getId(), amount, stock.getStockAmount(), Thread.currentThread().getId());

            //stockService.decreaseStock(stock, amount, dessertItem);
            orderService.createOrder(customUserDetails, orderRequestDto);

//            log.info("재고 감소 후: stockId={}, 남은 재고={}, threadId={}", stock.getId(), stock.getStockAmount(), Thread.currentThread().getId());
        } catch (InterruptedException e) {
           // log.error("InterruptedException 발생: stockId={}, threadId={}", stock.getId(), Thread.currentThread().getId(), e);
            throw new RuntimeException(e);
        } catch (Exception e) {
//            log.error("예기치 않은 예외 발생: stockId={}, threadId={}", stock.getId(), Thread.currentThread().getId(), e);
            throw e;
        } finally {
            lock.unlock();
        }
    }

}
