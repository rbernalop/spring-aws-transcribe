package org.rbernalop.springawstranscribe.audio.infrastructure;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.AllArgsConstructor;
import org.rbernalop.springawstranscribe.audio.infrastructure.configuration.AWSConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@AllArgsConstructor
public class S3BucketManager {
    private final AWSConfig awsConfig;

    private AmazonS3 s3Client() {
        return AmazonS3ClientBuilder.standard().withCredentials(awsConfig.awsStaticCredentialsProvider())
                .withRegion(awsConfig.getRegion()).build();
    }

    public void uploadFile(MultipartFile file, String bucketName) {
        String fileId = getFileId(file);
        try {
            s3Client().putObject(bucketName, fileId, file.getInputStream(), null);
        } catch (SdkClientException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFile(String fileId, String bucketName) {
        s3Client().deleteObject(bucketName, fileId);
    }

    public String getBucketUrl(String fileId, String bucketName) {
        return s3Client().getUrl(bucketName, fileId).toExternalForm();
    }

    public String getFileId(MultipartFile file) {
        return Objects.requireNonNull(file.getOriginalFilename()).replaceAll(" ", "_").toLowerCase();
    }
}
