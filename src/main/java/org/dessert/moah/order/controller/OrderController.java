package org.dessert.moah.order.controller;

import lombok.RequiredArgsConstructor;
import org.dessert.moah.common.dto.CommonResponseDto;
import org.dessert.moah.common.dto.ResultDto;
import org.dessert.moah.order.facade.OptimisticLockStockFacade;
import org.dessert.moah.order.facade.PessimisticLockOrderFacade;
import org.dessert.moah.order.facade.RedissonLockStockFacade;
import org.dessert.moah.order.service.OrderLockService;
import org.dessert.moah.user.dto.CustomUserDetails;
import org.dessert.moah.order.dto.OrderRequestDto;
import org.dessert.moah.order.dto.OrderResponseDto;
import org.dessert.moah.order.dto.OrderResponseList;
import org.dessert.moah.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moah/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderLockService orderLockService;
    private final RedissonLockStockFacade redissonLockStockFacade;
    private final OptimisticLockStockFacade optimisticLockStockFacade;
    private final PessimisticLockOrderFacade pessimisticLockOrderFacade;

    // 주문하기
//    @PostMapping
//    public ResponseEntity<ResultDto<Void>> createOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody OrderRequestDto orderRequestDto) {
//        CommonResponseDto<Object> commonResponseDto =
//                orderService.createOrder(customUserDetails, orderRequestDto);
//
//         //orderLockService.createOrder(customUserDetails, orderRequestDto);
//
//        ResultDto<Void> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
//
//        return ResponseEntity.status(commonResponseDto.getHttpStatus())
//                             .body(resultDto);
//    }

    @PostMapping
    public void createOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody OrderRequestDto orderRequestDto) throws Exception {
        //redissonLockStockFacade.decrease(customUserDetails, orderRequestDto);
       // optimisticLockStockFacade.decrease(customUserDetails,orderRequestDto);
        pessimisticLockOrderFacade.processOrder(customUserDetails,orderRequestDto);
    }

    // 주문 조회
/*    @GetMapping
    public ResponseEntity<CommonResponseDto<List<OrderResponseDto>>> getOrders(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam int page,
            @RequestParam int size) {
        CommonResponseDto<List<OrderResponseDto>> response = orderService.getOrders(customUserDetails, page, size);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }*/

    // 주문 전체 조회
    @GetMapping
    public ResponseEntity<ResultDto<OrderResponseList>> getOrders(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam int page, @RequestParam int size) {
        CommonResponseDto<Object> commonResponseDto = orderService.getOrders(customUserDetails, page, size);
        ResultDto<OrderResponseList> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
        resultDto.setData((OrderResponseList) commonResponseDto.getData());

        return ResponseEntity.status(commonResponseDto.getHttpStatus())
                             .body(resultDto);
    }

    // 주문 상세 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<ResultDto<OrderResponseDto>> getOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long orderId) {
        CommonResponseDto<Object> commonResponseDto = orderService.getOrder(customUserDetails, orderId);
        ResultDto<OrderResponseDto> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
        resultDto.setData((OrderResponseDto) commonResponseDto.getData());

        return ResponseEntity.status(commonResponseDto.getHttpStatus())
                             .body(resultDto);
    }


    // 주문 취소
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<ResultDto<Void>> cancelOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long orderId) {
        CommonResponseDto<Object> commonResponseDto = orderService.cancelOrder(customUserDetails, orderId);
        ResultDto<Void> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());

        return ResponseEntity.status(commonResponseDto.getHttpStatus())
                             .body(resultDto);
    }

    // 반품
    @PutMapping("/{orderId}/return")
    public ResponseEntity<ResultDto<Void>> requestReturn(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long orderId) {
        CommonResponseDto<Object> commonResponseDto = orderService.requestReturn(customUserDetails, orderId);
        ResultDto<Void> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());


        return ResponseEntity.status(commonResponseDto.getHttpStatus())
                             .body(resultDto);
    }


}
