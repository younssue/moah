package org.dessert.moah.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dessert.moah.common.dto.CommonResponseDto;
import org.dessert.moah.common.exception.NotFoundException;
import org.dessert.moah.common.exception.OutOfStockException;
import org.dessert.moah.common.service.CommonService;
import org.dessert.moah.common.type.ErrorCode;
import org.dessert.moah.common.type.SuccessCode;
import org.dessert.moah.item.entity.DessertItem;
import org.dessert.moah.item.entity.Stock;
import org.dessert.moah.item.repository.DessertItemRepository;
import org.dessert.moah.order.dto.OrderRequestDto;
import org.dessert.moah.order.entity.OrderItem;
import org.dessert.moah.order.entity.Orders;
import org.dessert.moah.order.repository.OrderItemRepository;
import org.dessert.moah.order.repository.OrderRepository;
import org.dessert.moah.order.type.OrderStatus;
import org.dessert.moah.user.dto.CustomUserDetails;
import org.dessert.moah.user.entity.Users;
import org.dessert.moah.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderLockService {
    private final CommonService commonService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final DessertItemRepository dessertItemRepository;
    private final StockLockService stockLockService;
    private final StockRedisAndLockService stockRedisAndLockService;

    // 주문 하기
   @Transactional
    public CommonResponseDto<Object> createOrder(CustomUserDetails customUserDetails, OrderRequestDto orderRequestDto) {
        // 유저 확인
        String email = customUserDetails.getEmail();
        Users user = userRepository.findByEmail(email)
                                   .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        // 상품 확인
        DessertItem dessertItem = dessertItemRepository.findById(orderRequestDto.getDessertId())
                                                       .orElseThrow(() -> new NotFoundException(ErrorCode.ITEM_NOT_FOUND));

        // 재고 확인 및 감소

        Stock stock = stockLockService.getStock(dessertItem.getStock()
                                                       .getId()); // stockId가 null이 아닌지 확인
        if (stock == null) {
            log.error("Stock is null for stockId: {}", stock.getId());
            throw new NotFoundException(ErrorCode.OUT_OF_STOCK);
        }

        // 재고 감소
        stockLockService.decreaseStock(stock.getId(), orderRequestDto.getCount(), dessertItem);



        OrderItem orderItem = OrderItem.builder()
                                       .orderPrice(dessertItem.getPrice())
                                       .count(orderRequestDto.getCount())
                                       .dessertItem(dessertItem)
                                       .build();


        Orders order = Orders.builder()
                             .users(user)
                             .orderStatus(OrderStatus.ORDER_COMPLETED) // 기본값 설정
                             .orderDate(LocalDateTime.now())
                             .orderItems(Collections.singletonList(orderItem))
                             .deliveryAddress(orderRequestDto.getDeliveryAddress())
                             .build();

        orderItemRepository.save(orderItem);
        // 주문 항목을 주문에 추가
        order.addOrderItem(orderItem);
        orderRepository.save(order);


        return commonService.successResponse(SuccessCode.EXAMPLE_SUCCESS.getDescription(), HttpStatus.OK, null);
    }

    @Transactional
    public Orders createOrder(Users user, DessertItem item, OrderRequestDto orderDto) {
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
       return orderRepository.save(order);
    }


    @Transactional(readOnly = true)
    public Users verifyUser(CustomUserDetails userDetails) {
        String email = userDetails.getEmail();
       return userRepository.findByEmail(email)
                                   .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public DessertItem verifyItem(Long dessertId) {
        // 상품 확인
        return dessertItemRepository.findById(dessertId)
                                                       .orElseThrow(() -> new NotFoundException(ErrorCode.ITEM_NOT_FOUND));
    }
}