package org.dessert.moah.item.controller;

import lombok.RequiredArgsConstructor;
import org.dessert.moah.common.dto.CommonResponseDto;
import org.dessert.moah.common.dto.ResultDto;
import org.dessert.moah.item.dto.ItemResponseDto;
import org.dessert.moah.item.dto.ItemResponseListDto;
import org.dessert.moah.item.dto.RemainStockDto;
import org.dessert.moah.item.dto.StockDto;
import org.dessert.moah.item.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moah/items")
public class ItemController {
    private final ItemService itemService;

    // 전체 리스트 조회
    @GetMapping
    public ResponseEntity<ResultDto<ItemResponseListDto>> getItemList(@RequestParam int page,@RequestParam int size){
        CommonResponseDto<Object> commonResponseDto = itemService.getItemList(page,size);
        ResultDto<ItemResponseListDto> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
        resultDto.setData((ItemResponseListDto) commonResponseDto.getData());

        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(resultDto);
    }

    // 상세 조회
    @GetMapping("/{dessertId}")
    public ResponseEntity<ResultDto<ItemResponseDto>> getItemDetail(@PathVariable Long dessertId){
        CommonResponseDto<Object> commonResponseDto = itemService.getItemDetail(dessertId);
        ResultDto<ItemResponseDto> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
        resultDto.setData((ItemResponseDto) commonResponseDto.getData());

        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(resultDto);
    }

    // 남은 수량 조회
    @GetMapping("/{dessertId}/remaining-quantity")
    public ResponseEntity<ResultDto<RemainStockDto>> checkRemainingStock(@PathVariable Long dessertId){
        CommonResponseDto<Object> commonResponseDto = itemService.checkRemainingStock(dessertId);
        ResultDto<RemainStockDto> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
        resultDto.setData((RemainStockDto) commonResponseDto.getData());

        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(resultDto);
    }
}
