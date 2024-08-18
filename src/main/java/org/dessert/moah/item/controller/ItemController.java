package org.dessert.moah.item.controller;

import lombok.RequiredArgsConstructor;
import org.dessert.moah.common.dto.CommonResponseDto;
import org.dessert.moah.common.dto.ResultDto;
import org.dessert.moah.item.dto.*;
import org.dessert.moah.item.entity.DessertItem;
import org.dessert.moah.item.entity.Stock;
import org.dessert.moah.item.service.ItemService;
import org.dessert.moah.item.type.DessertType;
import org.dessert.moah.item.type.SaleStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<String> saveDessertItem(
            @RequestPart("data") ItemRequestDto2 dessertDto,
            @RequestPart("file") List<MultipartFile> images) throws IOException, ExecutionException, InterruptedException {



        itemService.saveDessertItem(dessertDto, images);
        return ResponseEntity.ok("상품 등록이 성공했습니다");
    }
}
