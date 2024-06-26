package org.dessert.moah.controller;

import lombok.RequiredArgsConstructor;
import org.dessert.moah.base.dto.CommonResponseDto;
import org.dessert.moah.base.dto.ResultDto;
import org.dessert.moah.dto.item.ItemResponseDto;
import org.dessert.moah.service.ExampleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExampleController {

    private final ExampleService exampleService;

    // 반환값이 없을 때
    @PostMapping("/example")
    public ResponseEntity<ResultDto<Void>> examplePost(){
        CommonResponseDto<Object> commonResponseDto = exampleService.examplePost();
        ResultDto<Void> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());

        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(resultDto);
    }

    // 반환값이 있을 때
    @GetMapping("/example")
    public ResponseEntity<ResultDto<ItemResponseDto>> exampleGet(){
        CommonResponseDto<Object> commonResponseDto = exampleService.exampleGet();
        ResultDto<ItemResponseDto> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
        resultDto.setData((ItemResponseDto) commonResponseDto.getData());

        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(resultDto);
    }
}
