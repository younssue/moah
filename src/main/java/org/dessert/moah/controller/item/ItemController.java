package org.dessert.moah.controller.item;

import lombok.RequiredArgsConstructor;
import org.dessert.moah.base.dto.CommonResponseDto;
import org.dessert.moah.base.dto.ResultDto;
import org.dessert.moah.dto.item.ItemResponseDto;
import org.dessert.moah.dto.item.ItemResponseListDto;
import org.dessert.moah.service.item.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moah")
public class ItemController {
    private final ItemService itemService;

    // 전체 리스트 조회
    @GetMapping("/item/list")
    public ResponseEntity<ResultDto<ItemResponseListDto>> getItemList(){
        CommonResponseDto<Object> commonResponseDto = itemService.getItemList();
        ResultDto<ItemResponseListDto> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
        resultDto.setData((ItemResponseListDto) commonResponseDto.getData());

        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(resultDto);
    }

    @GetMapping("/item/list/{dessertId}")
    public ResponseEntity<ResultDto<ItemResponseDto>> getItemDetail(@PathVariable Long dessertId){
        CommonResponseDto<Object> commonResponseDto = itemService.getItemDetail(dessertId);
        ResultDto<ItemResponseDto> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
        resultDto.setData((ItemResponseDto) commonResponseDto.getData());

        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(resultDto);
    }
}
