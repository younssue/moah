package org.dessert.moah.controller.order;

import lombok.RequiredArgsConstructor;
import org.dessert.moah.base.dto.CommonResponseDto;
import org.dessert.moah.base.dto.ResultDto;
import org.dessert.moah.dto.CustomUserDetails;
import org.dessert.moah.dto.order.OrderRequestDto;
import org.dessert.moah.dto.order.OrderResponseList;
import org.dessert.moah.service.order.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moah/orders")
public class OrderController {
    private final OrderService orderService;

    // 주문하기
    @PostMapping
    public ResponseEntity<ResultDto<Void>> createOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody OrderRequestDto orderRequestDto) {
        CommonResponseDto<Object> commonResponseDto = orderService.createOrder(customUserDetails, orderRequestDto);
        ResultDto<Void> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());

        return ResponseEntity.status(commonResponseDto.getHttpStatus())
                             .body(resultDto);
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

    @GetMapping
    public ResponseEntity<ResultDto<OrderResponseList>> getOrders(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam int page,
                                                                  @RequestParam int size) {
        CommonResponseDto<Object> commonResponseDto = orderService.getOrders(customUserDetails, page, size);
        ResultDto<OrderResponseList> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
        resultDto.setData((OrderResponseList) commonResponseDto.getData());

        return ResponseEntity.status(commonResponseDto.getHttpStatus())
                             .body(resultDto);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<ResultDto<Void>> cancelOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long orderId) {
        CommonResponseDto<Object> commonResponseDto = orderService.cancelOrder(customUserDetails, orderId);
        ResultDto<Void> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());

        return ResponseEntity.status(commonResponseDto.getHttpStatus())
                             .body(resultDto);
    }

    @PutMapping("/{orderId}/return")
    public ResponseEntity<ResultDto<Void>> requestReturn(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long orderId) {
        CommonResponseDto<Object> commonResponseDto = orderService.requestReturn(customUserDetails, orderId);
        ResultDto<Void> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());


        return ResponseEntity.status(commonResponseDto.getHttpStatus())
                             .body(resultDto);
    }

}
