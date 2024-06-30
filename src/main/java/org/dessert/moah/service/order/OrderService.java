package org.dessert.moah.service.order;

import lombok.RequiredArgsConstructor;
import org.dessert.moah.base.dto.CommonResponseDto;
import org.dessert.moah.base.service.CommonService;
import org.dessert.moah.base.type.ErrorCode;
import org.dessert.moah.base.type.SuccessCode;
import org.dessert.moah.dto.CustomUserDetails;
import org.dessert.moah.dto.order.OrderItemDto;
import org.dessert.moah.dto.order.OrderRequestDto;
import org.dessert.moah.dto.order.OrderResponseDto;
import org.dessert.moah.dto.order.OrderResponseList;
import org.dessert.moah.entity.Item.DessertItem;
import org.dessert.moah.entity.Item.Stock;
import org.dessert.moah.entity.order.OrderItem;
import org.dessert.moah.entity.order.Orders;
import org.dessert.moah.entity.type.OrderStatus;
import org.dessert.moah.entity.user.Users;
import org.dessert.moah.exception.BadRequestException;
import org.dessert.moah.exception.NotFoundException;
import org.dessert.moah.exception.OutOfStockException;
import org.dessert.moah.repository.item.DessertItemRepository;
import org.dessert.moah.repository.order.OrderItemRepository;
import org.dessert.moah.repository.order.OrderRepository;
import org.dessert.moah.repository.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CommonService commonService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final DessertItemRepository dessertItemRepository;

    // 주문 하기
    public CommonResponseDto<Object> createOrder(CustomUserDetails customUserDetails, OrderRequestDto orderRequestDto) {
        // 유저 확인
        String email = customUserDetails.getEmail();
        Users user = userRepository.findByEmail(email)
                                   .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        // 상품 확인
        DessertItem dessertItem = dessertItemRepository.findById(orderRequestDto.getDessertId())
                                                       .orElseThrow(() -> new NotFoundException(ErrorCode.ITEM_NOT_FOUND));

        // 재고 확인 및 감소
        Stock stock = dessertItem.getStock();
        if (stock.getStockAmount() < orderRequestDto.getCount()) {
            throw new OutOfStockException(ErrorCode.OUT_OF_STOCK);
        }
        stock.decreaseStock(orderRequestDto.getCount());


        // 주문 생성

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
                             .build();

        orderItemRepository.save(orderItem);
        // 주문 항목을 주문에 추가
        order.addOrderItem(orderItem);
        orderRepository.save(order);


        return commonService.successResponse(SuccessCode.EXAMPLE_SUCCESS.getDescription(), HttpStatus.OK, null);
    }

    public CommonResponseDto<Object> getOrders(CustomUserDetails customUserDetails, int page, int size) {
        String email = customUserDetails.getEmail();
        Users user = userRepository.findByEmail(email)
                                   .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));


        List<Orders> ordersList = orderRepository.findOrdersByUserId(user.getId(), page, size);

        List<OrderResponseDto> orderResponseDtoList = ordersList.stream()
                                                                .map(order -> OrderResponseDto.builder()
                                                                                              .orderId(order.getId())
                                                                                              .orderStatus(order.getOrderStatus()
                                                                                                                .getDescription())
                                                                                              .orderDate(order.getOrderDate())
                                                                                              .totalPrice(order.getTotalPrice())
                                                                                              .orderItemDtoList(order.getOrderItems()
                                                                                                                     .stream()
                                                                                                                     .map(item -> OrderItemDto.builder()
                                                                                                                                              .orderItemId(item.getId())
                                                                                                                                              .dessertId(item.getDessertItem()
                                                                                                                                                             .getId())
                                                                                                                                              .dessertName(item.getDessertItem()
                                                                                                                                                               .getDessertName())
                                                                                                                                              .count(item.getCount())
                                                                                                                                              .price(item.getOrderPrice())
                                                                                                                                              .build())
                                                                                                                     .collect(Collectors.toList()))
                                                                                              .build())
                                                                .collect(Collectors.toList());

        OrderResponseList orderResponseListDto = OrderResponseList.builder()
                                                                  .orderResponseDtoList(orderResponseDtoList)
                                                                  .build();
        return commonService.successResponse(SuccessCode.EXAMPLE_SUCCESS.getDescription(), HttpStatus.OK, orderResponseListDto);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void updateOrderStatus() {
        List<Orders> ordersList = orderRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Orders order : ordersList) {
            if (order.getOrderStatus() == OrderStatus.ORDER_COMPLETED) {
                if (order.getOrderDate()
                         .plusDays(1)
                         .isBefore(now)) {
                    orderRepository.updateOrderStatus(order.getId(), OrderStatus.BEING_DELIVERED);
                }
            } else if (order.getOrderStatus() == OrderStatus.BEING_DELIVERED) {
                if (order.getOrderDate()
                         .plusDays(2)
                         .isBefore(now)) {
                    orderRepository.updateOrderStatus(order.getId(), OrderStatus.DELIVERY_COMPLETED);
                }
            }
        }
    }

    @Transactional
    public CommonResponseDto<Object> cancelOrder(CustomUserDetails customUserDetails, Long orderId) {
        String email = customUserDetails.getEmail();
        Users user = userRepository.findByEmail(email)
                                   .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Orders order = orderRepository.findByIdAndUsers(orderId, user)
                                      .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        // 주문 상태가 배송 중이 되기 이전까지만 취소 가능
        if (order.getOrderStatus() != OrderStatus.ORDER_COMPLETED) {
            throw new BadRequestException(ErrorCode.INVALID_ORDER_STATUS);
        }

        // 재고 복구
        for (OrderItem orderItem : order.getOrderItems()) {
            Stock stock = orderItem.getDessertItem()
                                   .getStock();
            stock.increaseStock(orderItem.getCount());
        }

        // 주문 상태를 취소 완료로 변경
        orderRepository.updateOrderStatus(order.getId(), OrderStatus.ORDER_CANCELLED);

        return commonService.successResponse(SuccessCode.ORDER_CANCELLED.getDescription(), HttpStatus.OK, null);
    }
}
