package org.dessert.moah.order.facade;

import lombok.RequiredArgsConstructor;
import org.dessert.moah.order.dto.OrderRequestDto;
import org.dessert.moah.order.service.OrderLockService;
import org.dessert.moah.order.service.OrderService;
import org.dessert.moah.order.service.StockLockService;
import org.dessert.moah.user.dto.CustomUserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OptimisticLockStockFacade {
    private final StockLockService stockLockService;
    private final OrderLockService orderLockService;

    public void decrease(CustomUserDetails customUserDetails, OrderRequestDto orderRequestDto) throws InterruptedException {
//        int maxRetries = 5; // 최대 재시도 횟수 설정
//        int currentAttempt = 0;

        while (true){
            try {
                orderLockService.createOrder(customUserDetails, orderRequestDto);

                break;
            }catch (Exception e){
                Thread.sleep(50);
            }
        }

    }

}
