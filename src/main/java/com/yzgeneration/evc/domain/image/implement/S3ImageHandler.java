package com.yzgeneration.evc.domain.image.implement;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.yzgeneration.evc.common.implement.port.UuidHolder;
import com.yzgeneration.evc.domain.image.dto.ImageResponse;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3ImageHandler implements ImageHandler {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private final UuidHolder uuidHolder;
    private static final long PRESIGNED_URL_VALID_TIME = 1000 * 60L * 3L;


    @Override
    public ImageResponse getPresignedURLForUpload(String prefix, String fileName) {
        if (!prefix.isBlank()) {
            fileName = createPath(prefix, fileName);
        }
        GeneratePresignedUrlRequest generatePresignedUrlRequest = generatePresignedURLForUpload(fileName);

        return ImageResponse.builder()
                .presignedURL(amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString())
                .imageName(fileName)
                .build();
    }

    @Override
    public void removeImage(String prefix, String fileName) {
        String key = prefix + "/" + fileName;
        log.info("deleteS3Image: {}", key);
        try {
            if (!amazonS3.doesObjectExist(bucket, key)) {
                log.info("deleteS3Image: {}", "객체가 없음");
                throw new CustomException(ErrorCode.IMAGE_NOT_FOUND);
            }
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, key));
        } catch (AmazonServiceException e) {
            log.info("deleteS3Image: {}", e.getMessage());
            throw new CustomException(ErrorCode.IMAGE_NOT_FOUND);
        }
    }

    private GeneratePresignedUrlRequest generatePresignedURLForUpload(String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, fileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(getPresignedURLExpiryTime());

        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicReadWrite.toString());

        return generatePresignedUrlRequest;
    }

    private Date getPresignedURLExpiryTime() {
        Date expiryTime = new Date();
        long expiryTimeMillis = expiryTime.getTime();
        expiryTimeMillis += PRESIGNED_URL_VALID_TIME;
        expiryTime.setTime(expiryTimeMillis);

        return expiryTime;
    }

    private String createPath(String prefix, String fileName) {
        String fileId = uuidHolder.random();
        return String.format("%s/%s", prefix, fileId + "-" + fileName);
    }

}
