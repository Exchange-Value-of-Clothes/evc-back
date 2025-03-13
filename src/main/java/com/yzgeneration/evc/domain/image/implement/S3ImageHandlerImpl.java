package com.yzgeneration.evc.domain.image.implement;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.yzgeneration.evc.common.implement.port.UuidHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class S3ImageHandlerImpl implements S3ImageHandler {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private final UuidHolder uuidHolder;
    private static final long PRESIGNED_URL_VALID_TIME = 1000 * 60L * 3L;


    @Override
    public String getPresignedURLForUpload(String prefix, String fileName) {
        if (!prefix.isBlank()) {
            fileName = createPath(prefix, fileName);
        }

        GeneratePresignedUrlRequest generatePresignedUrlRequest = generatePresignedURLForUpload(fileName);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toString();
    }

    @Override
    public String getImageUrl(String fileName) {
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private GeneratePresignedUrlRequest generatePresignedURLForUpload(String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, fileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(getPresignedURLExpiryTime());

        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString()
        );
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
