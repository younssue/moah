package org.dessert.moah.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // null 필드 제외
public class CommonResponseDto<T> {

    private String status;
    private String message;
    private HttpStatus httpStatus;
    private T data;
}
