package org.dessert.moah.order.facade;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dessert.moah.common.exception.NotFoundException;
import org.dessert.moah.common.type.ErrorCode;
import org.dessert.moah.item.dto.DessertDto;
import org.dessert.moah.item.entity.DessertItem;
import org.dessert.moah.item.entity.Stock;
import org.dessert.moah.item.repository.DessertItemRepository;
import org.dessert.moah.item.repository.StockRepository;
import org.dessert.moah.order.dto.OrderRequestDto;
import org.dessert.moah.order.entity.OrderItem;
import org.dessert.moah.order.entity.Orders;
import org.dessert.moah.order.repository.OrderItemRepository;
import org.dessert.moah.order.repository.OrderRepository;
import org.dessert.moah.order.service.OrderLockService;
import org.dessert.moah.order.service.OrderService;
import org.dessert.moah.order.service.StockLockService;
import org.dessert.moah.order.service.StockService;
import org.dessert.moah.order.type.OrderStatus;
import org.dessert.moah.user.dto.CustomUserDetails;
import org.dessert.moah.user.entity.Users;
import org.dessert.moah.user.repository.UserRepository;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedissonLockStockFacade {
    private final RedissonClient redissonClient;

    private final StockService stockService;
    private final OrderService orderService;
    private final UserRepository userRepository;
    private final DessertItemRepository dessertItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;




    public void decrease(CustomUserDetails userDetails, OrderRequestDto orderDto) throws InterruptedException {
        RLock lock = redissonClient.getLock(orderDto.getDessertId().toString());

        try {
            boolean available = lock.tryLock(15, 1, TimeUnit.SECONDS);

            if (!available) {
                System.out.println("lock 획득 실패");
                return;
            }
            //orderService.createOrder(customUserDetails, orderRequestDto);

            // 유저 확인 및 상품 확인
            Users user = userRepository.findByEmail(userDetails.getEmail())
                                       .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
            DessertDto dessertDto = dessertItemRepository.findDessertItem(orderDto.getDessertId())
                                                         .orElseThrow(() -> new NotFoundException(ErrorCode.ITEM_NOT_FOUND));

            // Stock stock = stockRepository.findByStockIdWithPessimisticLock(item.getStock().getId()).get();
            Stock stock = dessertDto.stock();
            System.out.println("stock redis 실행: " + dessertDto.stock().getId());

            stock.decreaseStock(orderDto.getCount(), dessertDto.dessertItem());
            stockRepository.saveAndFlush(stock);

            System.out.println("redis 정상 저장 완료: " + stock.getStockAmount());




            // 주문 항목 생성
            OrderItem orderItem = OrderItem.builder()
                                           .orderPrice(dessertDto.dessertItem().getPrice())
                                           .count(orderDto.getCount())
                                           .dessertItem(dessertDto.dessertItem())
                                           .build();

            // 주문 생성
            Orders order = Orders.builder()
                                 .users(user)
                                 .orderStatus(OrderStatus.ORDER_COMPLETED)
                                 .orderDate(LocalDateTime.now())
                                 .orderItems(Collections.singletonList(orderItem))
                                 .deliveryAddress(orderDto.getDeliveryAddress())
                                 .build();

            orderItemRepository.save(orderItem);
            // 주문 항목을 주문에 추가
            order.addOrderItem(orderItem);
            orderRepository.save(order);

        }catch (Exception e) {
            throw e;
        } finally {
            lock.unlock();
        }
    }

}
