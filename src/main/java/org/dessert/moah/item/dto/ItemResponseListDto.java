package org.dessert.moah.item.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
@Builder
public class ItemResponseListDto {
    private List<ItemResponseDto> itemResponseDtoList;
}
