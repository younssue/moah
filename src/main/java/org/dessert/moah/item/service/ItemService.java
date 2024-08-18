package org.dessert.moah.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.dessert.moah.common.config.s3upload.S3UploadService;
import org.dessert.moah.common.dto.CommonResponseDto;
import org.dessert.moah.common.service.CommonService;
import org.dessert.moah.common.type.SuccessCode;
import org.dessert.moah.item.dto.*;
import org.dessert.moah.item.entity.DessertItem;
import org.dessert.moah.item.entity.DessertItemImage;
import org.dessert.moah.item.entity.Stock;
import org.dessert.moah.item.repository.DessertItemImageRepository;
import org.dessert.moah.item.repository.DessertItemRepository;
import org.dessert.moah.item.repository.StockRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.dessert.moah.item.entity.QDessertItem.dessertItem;
@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final CommonService commonService;
    private final DessertItemRepository dessertItemRepository;
    private final StockRepository stockRepository;
    private final DessertItemImageRepository dessertItemImageRepository;
    private final S3UploadService s3UploadService;
    private final LocalStorageService localStorageService;


    public CommonResponseDto<Object> getItemList(int page, int size) {
        List<DessertItem> dessertItems = dessertItemRepository.findDessertItem(page, size);
        List<ItemResponseDto> itemResponseDtos = new ArrayList<>();

        for (DessertItem dessertItem : dessertItems) {
            String mainImgPath = dessertItem.getDessertItemImages()
                                            .get(0)
                                            .getImg_url();

            //String mainImgPath = dessertItem.getDessertItemImage().getImg_url();
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

        //String mainImgPath = dessertItem.getDessertItemImage().getImg_url();

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

    @Transactional(readOnly = true)
    public CommonResponseDto<Object> checkRemainingStock(Long dessertId) {
        DessertItem dessertItem = dessertItemRepository.findByIdAndDeletedAtIsNull(dessertId);


        Optional<Stock> stock = stockRepository.findById(dessertItem.getStock()
                                                                    .getId());
        RemainStockDto remainStockDto = RemainStockDto.builder()
                                                      .dessertId(dessertId)
                                                      .dessertName(dessertItem.getDessertName())
                                                      .stockAmount(stock.get()
                                                                        .getStockAmount())
                                                      .build();

        return commonService.successResponse(SuccessCode.STOCK_SUCCESS.getDescription(), HttpStatus.OK, remainStockDto);
    }

    @Transactional
    public ResponseEntity<String> saveDessertItem(ItemRequestDto2 dessertDto, List<MultipartFile> images) throws IOException, ExecutionException, InterruptedException {

        log.info("Starting saveDessertItem in thread [{}]", Thread.currentThread().getName());

        DessertItem dessertItem = DessertItem.builder()
                                             .dessertName(dessertDto.getDessertName())
                                             .contents(dessertDto.getContents())
                                             .price(dessertDto.getPrice())
                                             .saleStatus(dessertDto.getSaleStatus())
                                             .dessertType(dessertDto.getDessertType())
                                             .build();

        DessertItem savedDessertItem = dessertItemRepository.save(dessertItem);

        Stock stock = Stock.builder()
                           .stockAmount(dessertDto.getStock().getStockAmount())
                           .sellAmount(dessertDto.getStock().getSellAmount())
                           .dessertItem(savedDessertItem)
                           .build();

        Stock savedStock = stockRepository.save(stock);
        savedDessertItem.setStock(savedStock);
        dessertItemRepository.save(savedDessertItem);

        // 비동기로 S3 이미지 업로드
        CompletableFuture<List<String>> imageUrlFuture = s3UploadService.uploadImgAsync(images, "ITEM");

        imageUrlFuture.thenAccept(imageUrls -> {
            log.info("Successfully uploaded images in thread [{}]", Thread.currentThread().getName());
            imageUrls.forEach(imageUrl -> {
                saveDessertItemImage(savedDessertItem, imageUrl);
            });

//            savedDessertItem.setDessertItemImages(dessertItemImageRepository.findByDessertItem(savedDessertItem));

        }).exceptionally(ex -> {
            log.error("Exception during image upload in thread [{}]", Thread.currentThread().getName(), ex);

            // 이미지 업로드 실패 시, 저장된 DessertItem 및 Stock 삭제 처리
            deleteDessertItemAndStock(savedDessertItem);
            throw new RuntimeException("이미지 업로드 실패로 인해 트랜잭션 롤백", ex);
        });

        // 동기적 S3 저장
/*        List<String> imageUrls = saveS3Img(images);
        log.info("동기 이미지 저장 시작 [{}]", Thread.currentThread().getName());
        imageUrls.forEach(imageUrl -> {
            saveDessertItemImage(savedDessertItem, imageUrl);
        });*/

        // 동기적으로 이미지 DB 저장
       /* List<String> imageUrls = saveImages(images);
        log.info("동기 이미지 저장 시작 [{}]", Thread.currentThread().getName());

        imageUrls.forEach(imageUrl -> {
            saveDessertItemImage(savedDessertItem, imageUrl);
        });*/



        log.info("Finished saveDessertItem in thread [{}]", Thread.currentThread().getName());


        return ResponseEntity.ok("상품 등록이 성공했습니다");
    }

    private void deleteDessertItemAndStock(DessertItem savedDessertItem) {
        try {
            Stock stock = savedDessertItem.getStock();
            if(stock != null){
                stockRepository.delete(stock);
            }
            // DessertItem 삭제
            dessertItemRepository.delete(savedDessertItem);
            log.info("이미지 업로드 실패 : 재고, 아이템 삭제 완료");

        }catch (Exception e){
            throw new RuntimeException("이미지 업로드 실패 : 재고 아이템 삭제 실패", e);
        }


         }

/*    private String saveImages(MultipartFile image) {

        // 이미지 저장 로직
        String imageUrl = "http://moah.com/images/" + image.getOriginalFilename();

        return imageUrl;

    }*/

    private List<String> saveImages(List<MultipartFile> images) {


        // 이미지 저장 로직
        List<String> imageUrlList= new ArrayList<>();
        for (MultipartFile image : images){
            String imageUrl = "http://moah.com/images/" + image.getOriginalFilename();
            imageUrlList.add(imageUrl);
        }

        return imageUrlList;

    }

    // 동기 s3 이미지 저장
    private List<String> saveS3Img(List<MultipartFile> imageList) {
        // s3에 이미지 업로드

       List<String> imgUrlList = s3UploadService.itemImgUploadOriginal(imageList);
        return imgUrlList;

    }


    @Transactional
    protected void saveDessertItemImage(DessertItem savedDessertItem, String imageUrl) {
        DessertItemImage dessertItemImage = DessertItemImage.builder()
                                                            .img_url(imageUrl)
                                                            .dessertItem(savedDessertItem)
                                                            .build();
        dessertItemImageRepository.save(dessertItemImage);
        log.info("Successfully saved DessertItemImage in thread [{}]", Thread.currentThread().getName());
    }

}
