package org.dessert.moah.item.service;

import lombok.RequiredArgsConstructor;
import org.dessert.moah.common.dto.CommonResponseDto;
import org.dessert.moah.common.service.CommonService;
import org.dessert.moah.common.type.SuccessCode;
import org.dessert.moah.item.dto.ItemResponseDto;
import org.dessert.moah.item.dto.ItemResponseListDto;
import org.dessert.moah.item.dto.StockDto;
import org.dessert.moah.item.entity.DessertItem;
import org.dessert.moah.item.repository.DessertItemRepository;
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
                                                             .dessertItemImg(mainImgPath)
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

        String mainImgPath = dessertItem.getDessertItemImages()
                                        .get(0)
                                        .getImg_url();

        ItemResponseDto itemResponseDto = ItemResponseDto.builder()
                                                         .stock(stockDto)
                                                         .contents(dessertItem.getContents())
                                                         .dessertName(dessertItem.getDessertName())
                                                         .price(dessertItem.getPrice())
                                                         .saleStatus(dessertItem.getSaleStatus())
                                                         .dessertType(dessertItem.getDessertType())
                                                         .dessert_id(dessertItem.getId())
                                                         .dessertItemImg(mainImgPath)
                                                         .build();
        return commonService.successResponse(SuccessCode.EXAMPLE_SUCCESS.getDescription(), HttpStatus.OK, itemResponseDto);
    }
}
