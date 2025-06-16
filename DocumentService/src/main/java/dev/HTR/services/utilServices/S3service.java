package dev.HTR.services.utilServices;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.io.IOException;

import java.time.Duration;

@Service
@AllArgsConstructor
public class S3service {

    private static final String BUCKET = "documentstaily";

    @Autowired
    private S3Client s3Client;

    @Autowired
    private S3Presigner s3Presigner;

    public String uploadFile(MultipartFile file, Long docId, int version) {
        String key = "documents/" + docId + "/v" + version + "/" + file.getOriginalFilename();

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(BUCKET)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
            return key;
        } catch (IOException e) {
            throw new RuntimeException("Upload failed", e);
        }
    }


    public String generatePresignedDownloadUrl(String objectKey) {
        System.out.println("generatePresignedDownloadUrl");

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET)
                .key(objectKey)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(builder -> builder
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofMinutes(10))
        );

        System.out.println(presignedRequest.url().toString());
        return presignedRequest.url().toString();
    }
}
