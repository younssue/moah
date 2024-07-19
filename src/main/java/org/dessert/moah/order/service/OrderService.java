package org.dessert.moah.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dessert.moah.common.dto.CommonResponseDto;
import org.dessert.moah.common.service.CommonService;
import org.dessert.moah.common.type.ErrorCode;
import org.dessert.moah.common.type.SuccessCode;
import org.dessert.moah.user.dto.CustomUserDetails;
import org.dessert.moah.order.dto.OrderItemDto;
import org.dessert.moah.order.dto.OrderRequestDto;
import org.dessert.moah.order.dto.OrderResponseDto;
import org.dessert.moah.order.dto.OrderResponseList;
import org.dessert.moah.item.entity.DessertItem;
import org.dessert.moah.item.entity.Stock;
import org.dessert.moah.order.entity.OrderItem;
import org.dessert.moah.order.entity.Orders;
import org.dessert.moah.order.type.OrderStatus;
import org.dessert.moah.user.entity.Users;
import org.dessert.moah.common.exception.BadRequestException;
import org.dessert.moah.common.exception.NotFoundException;
import org.dessert.moah.common.exception.OutOfStockException;
import org.dessert.moah.item.repository.DessertItemRepository;
import org.dessert.moah.order.repository.OrderItemRepository;
import org.dessert.moah.order.repository.OrderRepository;
import org.dessert.moah.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final CommonService commonService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final DessertItemRepository dessertItemRepository;
    private final StockService stockService;

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
/*        Long stockId= dessertItem.getStock().getId();
        System.out.println("캐싱처리 성공이라면 select stock 쿼리 발생 하지 않는다");

        Stock stock = stockService.getStock(stockId);

        System.out.println("캐싱처리 성공이라면 select stock 쿼리 발생 하지 않는다2");*/
        Long stockId = dessertItem.getStock().getId();
        log.info("재고 확인을 위한 stockId: {}", stockId);

        Stock stock = stockService.getStock(stockId); // stockId가 null이 아닌지 확인
        if (stock == null) {
            log.error("Stock is null for stockId: {}", stockId);
            throw new NotFoundException(ErrorCode.OUT_OF_STOCK);
        }


        if (stock.getStockAmount() < orderRequestDto.getCount()) {
            throw new OutOfStockException(ErrorCode.OUT_OF_STOCK);
        }
       // stock.decreaseStock(orderRequestDto.getCount());

        stockService.decreaseStock(stock,orderRequestDto.getCount() ,dessertItem);

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
                             .deliveryAddress(orderRequestDto.getDeliveryAddress())
                             .build();

        orderItemRepository.save(orderItem);
        // 주문 항목을 주문에 추가
        order.addOrderItem(orderItem);
        orderRepository.save(order);


        return commonService.successResponse(SuccessCode.EXAMPLE_SUCCESS.getDescription(), HttpStatus.OK, null);
    }

    // 유저의 전체 주문 내역
    public CommonResponseDto<Object> getOrders(CustomUserDetails customUserDetails, int page, int size) {
        String email = customUserDetails.getEmail();
        Users user = userRepository.findByEmail(email)
                                   .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));


        List<Orders> ordersList = orderRepository.findOrdersByUserId(user.getId(), page, size);
        List<OrderResponseDto> orderResponseDtoList = new ArrayList<>();

        for (Orders order : ordersList) {

            List<OrderItemDto> orderItemDtoList = order.getOrderItems()
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
                                                       .collect(Collectors.toList());

            OrderResponseDto orderResponseDto = OrderResponseDto.builder()
                                                                .orderId(order.getId())
                                                                .orderStatus(order.getOrderStatus()
                                                                                  .getDescription())
                                                                .deliveryAddress(order.getDeliveryAddress())
                                                                .orderDate(order.getOrderDate())
                                                                .totalPrice(order.getTotalPrice())
                                                                .orderItemDtoList(orderItemDtoList)

                                                                .build();
            orderResponseDtoList.add(orderResponseDto);

        }
        OrderResponseList orderResponseListDto = OrderResponseList.builder()
                                                                  .orderResponseDtoList(orderResponseDtoList)
                                                                  .build();
        return commonService.successResponse(SuccessCode.EXAMPLE_SUCCESS.getDescription(), HttpStatus.OK, orderResponseListDto);
    }

    // 단일 주문 내역
    public CommonResponseDto<Object> getOrder(CustomUserDetails customUserDetails, Long orderId) {
        String email = customUserDetails.getEmail();
        Users user = userRepository.findByEmail(email)
                                   .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Orders order = orderRepository.findByIdAndUsers(orderId, user)
                                      .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        List<OrderItemDto> orderItemDtoList = order.getOrderItems()
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
                                                   .collect(Collectors.toList());

        OrderResponseDto orderResponseDto = OrderResponseDto.builder()
                                                            .orderId(order.getId())
                                                            .orderStatus(order.getOrderStatus()
                                                                              .getDescription())
                                                            .orderDate(order.getOrderDate())
                                                            .totalPrice(order.getTotalPrice())
                                                            .orderItemDtoList(orderItemDtoList)
                                                            .deliveryAddress(order.getDeliveryAddress())
                                                            .build();

        return commonService.successResponse(SuccessCode.EXAMPLE_SUCCESS.getDescription(), HttpStatus.OK, orderResponseDto);
    }

    // 주문 상태 업데이트 (매일 자정에 실행)
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

    // 주문 취소
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

    // 상품 반품 신청
    @Transactional
    public CommonResponseDto<Object> requestReturn(CustomUserDetails customUserDetails, Long orderId) {

        String email = customUserDetails.getEmail();
        Users user = userRepository.findByEmail(email)
                                   .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Orders order = orderRepository.findByIdAndUsers(orderId, user)
                                      .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getOrderStatus() != OrderStatus.DELIVERY_COMPLETED) {
            throw new BadRequestException(ErrorCode.INVALID_ORDER_STATUS);
        }

        if (order.getOrderDate()
                 .plusDays(3)
                 .isBefore(LocalDateTime.now())) {
            throw new BadRequestException(ErrorCode.RETURN_PERIOD_EXPIRED);
        }

        orderRepository.updateOrderStatus(order.getId(), OrderStatus.REQUEST_FOR_RETURN);

        return commonService.successResponse(SuccessCode.RETURN_SUCCESS.getDescription(), HttpStatus.OK, null);
    }

    // 반품 상태 업데이트 (매일 자정에 실행)
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    //@Scheduled(cron = "0 0/1 * * * ?") // 매 1분마다 실행
    public void updateReturnStatus() {
        List<Orders> ordersList = orderRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Orders order : ordersList) {
            if (order.getOrderStatus() == OrderStatus.REQUEST_FOR_RETURN) {
                if (order.getOrderDate()
                         .plusDays(1)
                         .isBefore(now)) {
                    orderRepository.updateOrderStatus(order.getId(), OrderStatus.RETURN_COMPLETED);
                    for (OrderItem item : order.getOrderItems()) {
                        item.getDessertItem()
                            .getStock()
                            .increaseStock(item.getCount());
                    }
                }
            }
        }
    }

}
