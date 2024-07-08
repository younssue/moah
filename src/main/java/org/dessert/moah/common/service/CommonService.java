package org.dessert.moah.common.service;


import org.dessert.moah.common.dto.CommonResponseDto;
import org.dessert.moah.common.type.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

    public CommonResponseDto<Object> successResponse(String message, HttpStatus httpStatus, Object data) {
        return CommonResponseDto.builder()
                                .httpStatus(httpStatus)
                                .status(ResponseStatus.SUCCESS.getDescription())
                                .message(message)
                                .data(data)
                                .build();
    }



    public CommonResponseDto<Object> errorResponse(String message, HttpStatus httpStatus, Object data) {
        return CommonResponseDto.builder()
                                .httpStatus(httpStatus)
                                .status(ResponseStatus.FAIL.getDescription())
                                .message(message)
                                .data(data)
                                .build();
    }
}
