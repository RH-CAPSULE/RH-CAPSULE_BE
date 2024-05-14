package com.rh.rh_capsule.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFileToS3(MultipartFile file, String userId, String capsuleBoxId, FileType type) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        String newFileName = "capsuleS3/" +  userId + "/" + capsuleBoxId + "/" + type.getType() + "/" + UUID.randomUUID().toString() + extension;

        amazonS3.putObject(bucket, newFileName, file.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, newFileName).toString();
    }

    public void deleteFileFromS3(String fileUrl) {
        try {
            String key = fileUrl.substring(fileUrl.indexOf(bucket) + bucket.length() + 1);

            amazonS3.deleteObject(new DeleteObjectRequest(bucket, key));
            log.info("Successfully deleted file [{}] from S3 bucket [{}]", key, bucket);
        } catch (Exception e) {
            log.error("Failed to delete file from S3 bucket [{}]: {}", bucket, e.getMessage(), e);
            throw new RuntimeException("Failed to delete file from S3", e);
        }
    }
}

