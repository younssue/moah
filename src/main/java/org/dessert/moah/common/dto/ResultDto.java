package org.dessert.moah.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor(staticName = "in")
@JsonInclude(JsonInclude.Include.NON_NULL) // null 필드 제외
public class ResultDto<Data> {

    private final String status;
    private final String message;

    @Setter
    private Data data;
}
