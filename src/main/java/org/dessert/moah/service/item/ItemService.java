package org.dessert.moah.service.item;

import lombok.RequiredArgsConstructor;
import org.dessert.moah.base.dto.CommonResponseDto;
import org.dessert.moah.base.service.CommonService;
import org.dessert.moah.base.type.SuccessCode;
import org.dessert.moah.dto.item.ItemResponseDto;
import org.dessert.moah.dto.item.ItemResponseListDto;
import org.dessert.moah.dto.item.StockDto;
import org.dessert.moah.entity.Item.DessertItem;
import org.dessert.moah.repository.item.DessertItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final CommonService commonService;
    private final DessertItemRepository dessertItemRepository;


    public CommonResponseDto<Object> getItemList() {
        List<DessertItem> dessertItems = dessertItemRepository.findByDeletedAtIsNull();
        List<ItemResponseDto> itemResponseDtos = new ArrayList<>();

        for (DessertItem dessertItem : dessertItems) {
            String mainImgPath = dessertItem.getDessertItemImages()
                                            .get(0)
                                            .getImg_url();

            StockDto stockDto = StockDto.builder()
                                        .stockId(dessertItem.getStock()
                                                            .getId())
                                        .stockAmount(dessertItem.getStock()
                                                                .getStockAmount())
                                        .sellAmount(dessertItem.getStock()
                                                               .getSellAmount())
                                        .build();

            ItemResponseDto itemResponseDto = ItemResponseDto.builder()
                                                             .dessert_id(dessertItem.getId())
                                                             .stock(stockDto)
                                                             .price(dessertItem.getPrice())
                                                             .dessertType(dessertItem.getDessertType())
                                                             .saleStatus(dessertItem.getSaleStatus())
                                                             .dessertName(dessertItem.getDessertName())
                                                             .contents(dessertItem.getContents())
                                                             .build();

            itemResponseDtos.add(itemResponseDto);
        }

        ItemResponseListDto itemResponseListDto = ItemResponseListDto.builder()
                                                                     .itemResponseDtoList(itemResponseDtos)
                                                                     .build();

        return commonService.successResponse(SuccessCode.EXAMPLE_SUCCESS.getDescription(), HttpStatus.OK, itemResponseListDto);
    }

    public CommonResponseDto<Object> getItemDetail(Long dessertId) {

        DessertItem dessertItem = dessertItemRepository.findByIdAndDeletedAtIsNull(dessertId);

        StockDto stockDto = StockDto.builder()
                                    .sellAmount(dessertItem.getStock()
                                                           .getSellAmount())
                                    .stockAmount(dessertItem.getStock()
                                                            .getStockAmount())
                                    .stockId(dessertItem.getStock()
                                                        .getId())
                                    .build();

        ItemResponseDto itemResponseDto = ItemResponseDto.builder()
                                                         .stock(stockDto)
                                                         .contents(dessertItem.getContents())
                                                         .dessertName(dessertItem.getDessertName())
                                                         .price(dessertItem.getPrice())
                                                         .saleStatus(dessertItem.getSaleStatus())
                                                         .dessertType(dessertItem.getDessertType())
                                                         .dessert_id(dessertItem.getId())
                                                         .build();
        return commonService.successResponse(SuccessCode.EXAMPLE_SUCCESS.getDescription(), HttpStatus.OK, itemResponseDto);
    }
}
