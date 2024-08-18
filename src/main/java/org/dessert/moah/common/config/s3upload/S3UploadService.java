package org.dessert.moah.common.config.s3upload;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dessert.moah.common.exception.BadRequestException;
import org.dessert.moah.common.exception.NotFoundException;
import org.dessert.moah.common.type.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
@Service
public class S3UploadService {
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final String ITEM_PATH = "ITEM";




    // 이미지 업로드
    @Async("imageUploadExecutor")
    public CompletableFuture<List<String>> itemImgUpload(List<MultipartFile> multipartFileList) {
        return uploadImgAsync(multipartFileList ,ITEM_PATH);
    }

    public List<String> itemImgUploadOriginal(List<MultipartFile> multipartFileList) {
        return uploadImgOriginal(multipartFileList ,ITEM_PATH);
    }

    // 여러 이미지 업로드
    public List<String> uploadImgOriginal(List<MultipartFile> multipartFiles, String folder) {
        List<String> imgUrlList = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            String fileName = createFileName(file.getOriginalFilename());

            try {
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                                    .bucket(bucket)
                                                                    .key(folder + "/" + fileName)
                                                                    .acl("public-read")
                                                                    .contentType(file.getContentType())
                                                                    .build();

                PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest,
                        RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

                if (putObjectResponse.sdkHttpResponse().isSuccessful()) {
                    imgUrlList.add(s3Client.utilities().getUrl(builder -> builder.bucket(bucket).key(folder + "/" + fileName)).toString());
                } else {
                    throw new BadRequestException(ErrorCode.IMAGE_UPLOAD_FAIL);
                }
            } catch (IOException e) {
                throw new BadRequestException(ErrorCode.IMAGE_UPLOAD_FAIL);
            }
        }
        return imgUrlList;
    }

    @Async("imageUploadExecutor")
    public CompletableFuture<List<String>> uploadImgAsync(List<MultipartFile> multipartFiles, String folder) {
        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                String fileName = createFileName(file.getOriginalFilename());

                try {
                    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                                        .bucket(bucket)
                                                                        .key(folder + "/" + fileName)
                                                                        .acl("public-read")
                                                                        .contentType(file.getContentType())
                                                                        .build();

                    PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest,
                            RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

                    if (putObjectResponse.sdkHttpResponse().isSuccessful()) {
                        return s3Client.utilities().getUrl(builder -> builder.bucket(bucket).key(folder + "/" + fileName)).toString();
                    } else {
                        throw new BadRequestException(ErrorCode.IMAGE_UPLOAD_FAIL);
                    }
                } catch (IOException e) {
                    throw new BadRequestException(ErrorCode.IMAGE_UPLOAD_FAIL);
                }
            });

            futures.add(future);
        }

        // 모든 비동기 작업이 완료되면 결과 리스트를 반환
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                                .thenApply(v -> futures.stream()
                                                       .map(CompletableFuture::join)
                                                       .collect(Collectors.toList()))
                                .exceptionally(ex -> {
                                    log.error("One or more image uploads failed", ex);
                                    throw new RuntimeException("One or more image uploads failed", ex);
                                });
    }


    // 단일 이미지 업로드
    @Async
    public CompletableFuture<String> uploadImg(MultipartFile multipartFile, String folder) {
        CompletableFuture<String> future = new CompletableFuture<>();
        String fileName = createFileName(multipartFile.getOriginalFilename());

        try {

            if("fail.png".equals(multipartFile.getOriginalFilename())){
                throw new IOException("error");
            }
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                                .bucket(bucket)
                                                                .key(folder + "/" + fileName)
                                                                .acl("public-read")
                                                                .contentType(multipartFile.getContentType())
                                                                .build();

            PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));

            if (putObjectResponse.sdkHttpResponse().isSuccessful()) {
                String imageUrl = s3Client.utilities().getUrl(builder -> builder.bucket(bucket).key(folder + "/" + fileName)).toString();
                future.complete(imageUrl); // 이미지 업로드 성공 시
                log.info("이미지 업로드 성공 thread:  [{}]", Thread.currentThread().getName());
            } else {
                future.completeExceptionally(new BadRequestException(ErrorCode.IMAGE_UPLOAD_FAIL)); // 이미지 업로드 실패 시
                log.info("이미지 업로드 실패 thread:  [{}]", Thread.currentThread().getName());
            }
        } catch (IOException e) {
            future.completeExceptionally(new BadRequestException(ErrorCode.IMAGE_UPLOAD_FAIL));
            log.error("Exception thread [{}]", Thread.currentThread().getName(), e);
        }

        return future;
    }


    public String uploadImgOriginal(MultipartFile multipartFile , String folder) {
        String fileName = createFileName(multipartFile.getOriginalFilename());

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                                .bucket(bucket)
                                                                .key(folder + "/" + fileName)
                                                                .acl("public-read")
                                                                .contentType(multipartFile.getContentType())
                                                                .build();

            PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));

            if (putObjectResponse.sdkHttpResponse().isSuccessful()) {
                 return s3Client.utilities().getUrl(builder -> builder.bucket(bucket).key(folder + "/" + fileName)).toString();

            } else {
                throw new BadRequestException(ErrorCode.IMAGE_UPLOAD_FAIL);
            }
        } catch (IOException e) {
            throw new BadRequestException(ErrorCode.IMAGE_UPLOAD_FAIL);
        }
    }


    // 이미지 삭제 (다중)
/*    public void deleteShopImg(String imgPath) {
        deleteImage(imgPath, ITEM_PATH);
    }*/



    // 이미지 삭제 (단일)
    public void deleteProfileImg(String imgPath) {
        deleteImage(imgPath, ITEM_PATH);
    }

    // 이미지 삭제
    public void deleteImage(String imgPath, String folder) {
        int lastIndex = imgPath.lastIndexOf("/") + 1;
        String substringImgPath = imgPath.substring(lastIndex);

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                                                                     .bucket(bucket)
                                                                     .key(folder + "/" + substringImgPath)
                                                                     .build();

        s3Client.deleteObject(deleteObjectRequest);
    }



    // 이미지 파일명 중복 방지
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // 파일 유효성 확인
    private String getFileExtension(String fileName) {

        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));

        if (!fileValidate.contains(idxFileName)) {
            throw new NotFoundException(ErrorCode.FILE_EXTENSION_NOT_FOUND);
        }

        return idxFileName;
    }
}


