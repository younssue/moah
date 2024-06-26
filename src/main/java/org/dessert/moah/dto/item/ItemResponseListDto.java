package org.dessert.moah.dto.item;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
@Builder
public class ItemResponseListDto {
    private List<ItemResponseDto> itemResponseDtoList;
}
