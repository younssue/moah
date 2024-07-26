package org.dessert.moah.order.facade;

import lombok.RequiredArgsConstructor;
import org.dessert.moah.common.exception.NotFoundException;
import org.springframework.dao.CannotAcquireLockException;
import org.dessert.moah.common.type.ErrorCode;
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
import org.dessert.moah.order.type.OrderStatus;
import org.dessert.moah.user.dto.CustomUserDetails;
import org.dessert.moah.user.entity.Users;
import org.dessert.moah.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PessimisticLockOrderFacade {
    //    private final StockLockService stockLockService;
    private final OrderLockService orderLockService;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    private final DessertItemRepository dessertItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;



    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void processOrder(CustomUserDetails userDetails, OrderRequestDto orderDto) throws Exception {
//        try {
        // 유저 확인 및 상품 확인
        Users user = userRepository.findByEmail(userDetails.getEmail())
                                   .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        DessertItem item = dessertItemRepository.findById(orderDto.getDessertId())
                                                .orElseThrow(() -> new NotFoundException(ErrorCode.ITEM_NOT_FOUND));

        Stock stock = stockRepository.findByStockIdWithPessimisticLock(item.getStock()
                                                                           .getId()).get();
        System.out.println("stock 락 실행: " + stock.getId());

        stock.decreaseStock(orderDto.getCount(), item);
        stockRepository.saveAndFlush(stock);

        System.out.println("정상 저장 완료: " + stock.getStockAmount());




        // 주문 항목 생성
        OrderItem orderItem = OrderItem.builder()
                                       .orderPrice(item.getPrice())
                                       .count(orderDto.getCount())
                                       .dessertItem(item)
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






//        } catch (Exception e) {
//            throw new Exception("Order processing failed", e);
//        }
    }



    /*@Transactional(isolation = Isolation.SERIALIZABLE)
    public void processOrder(CustomUserDetails userDetails, OrderRequestDto orderDto) throws Exception {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        int retryCount = 5;  // 재시도 횟수
        int backoffTime = 100;  // 초기 대기 시간

        while (retryCount > 0) {
            try {
                // 유저 확인 및 상품 확인
                Users user = userRepository.findByEmail(userDetails.getEmail())
                                           .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
                DessertItem item = dessertItemRepository.findById(orderDto.getDessertId())
                                                        .orElseThrow(() -> new NotFoundException(ErrorCode.ITEM_NOT_FOUND));

                // 재고 감소
                Stock stock = stockRepository.findByStockIdWithPessimisticLock(item.getStock().getId())
                                             .orElseThrow(() -> new NotFoundException(ErrorCode.STOCK_NOT_FOUND));
                stock.decreaseStock(orderDto.getCount(), item);
                stockRepository.save(stock);

                // 주문 생성
                OrderItem orderItem = OrderItem.builder()
                                               .orderPrice(item.getPrice())
                                               .count(orderDto.getCount())
                                               .dessertItem(item)
                                               .build();

                Orders order = Orders.builder()
                                     .users(user)
                                     .orderStatus(OrderStatus.ORDER_COMPLETED)
                                     .orderDate(LocalDateTime.now())
                                     .orderItems(Collections.singletonList(orderItem))
                                     .deliveryAddress(orderDto.getDeliveryAddress())
                                     .build();

                orderItemRepository.save(orderItem);
                order.addOrderItem(orderItem);
                orderRepository.save(order);

                return;

            } catch ( PessimisticLockingFailureException e) {
                logger.warn("데드락이 감지되었습니다. 재시도 횟수: " + (6 - retryCount));
                retryCount--;
                if (retryCount == 0) {
                    throw e;  // 재시도 실패 시 예외를 다시 던져 트랜잭션 롤백
                }
                Thread.sleep(backoffTime);  // 점진적 백오프 대기 후 재시도
                backoffTime *= 2;  // 대기 시간을 점진적으로 늘림
            } catch (Exception e) {
                logger.error(userDetails.getEmail() + "유저에 대한 처리가 실패했습니다.", e);
                throw e;  // 예외를 다시 던져 트랜잭션 롤백
            }
        }
    }*/

    /*@Transactional(isolation = Isolation.SERIALIZABLE)
    public void processOrderWithRetry(CustomUserDetails userDetails, OrderRequestDto orderDto) throws Exception {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        int retryCount = 5;  // 재시도 횟수
        int backoffTime = 100;  // 초기 대기 시간
        while (retryCount > 0) {
            try {
                processOrder(userDetails, orderDto);
                return; // 성공 시 루프 종료
            } catch (PessimisticLockingFailureException e) {
                logger.warn("데드락이 감지되었습니다. 재시도 횟수: " + (6 - retryCount));
                retryCount--;
                if (retryCount == 0) {
                    throw e;  // 재시도 실패 시 예외를 다시 던져 트랜잭션 롤백
                }
                Thread.sleep(backoffTime);  // 점진적 백오프 대기 후 재시도
                backoffTime *= 2;  // 대기 시간을 점진적으로 늘림
            } catch (Exception e) {
                logger.error(userDetails.getEmail() + " 유저에 대한 처리가 실패했습니다.", e);
                throw e;  // 예외를 다시 던져 트랜잭션 롤백
            }
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public void processOrder(CustomUserDetails userDetails, OrderRequestDto orderDto) throws Exception {
        // 유저 확인 및 상품 확인
        Users user = userRepository.findByEmail(userDetails.getEmail())
                                   .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        DessertItem item = dessertItemRepository.findById(orderDto.getDessertId())
                                                .orElseThrow(() -> new NotFoundException(ErrorCode.ITEM_NOT_FOUND));
        // 재고 감소
        Stock stock = stockRepository.findByStockIdWithPessimisticLock(item.getStock().getId())
                                     .orElseThrow(() -> new NotFoundException(ErrorCode.STOCK_NOT_FOUND));
        stock.decreaseStock(orderDto.getCount(), item);
        stockRepository.save(stock);
        // 주문 생성
        OrderItem orderItem = OrderItem.builder()
                                       .orderPrice(item.getPrice())
                                       .count(orderDto.getCount())
                                       .dessertItem(item)
                                       .build();
        Orders order = Orders.builder()
                             .users(user)
                             .orderStatus(OrderStatus.ORDER_COMPLETED)
                             .orderDate(LocalDateTime.now())
                             .orderItems(Collections.singletonList(orderItem))
                             .deliveryAddress(orderDto.getDeliveryAddress())
                             .build();
        orderItemRepository.save(orderItem);
        order.addOrderItem(orderItem);
        orderRepository.save(order);
    }*/








}
