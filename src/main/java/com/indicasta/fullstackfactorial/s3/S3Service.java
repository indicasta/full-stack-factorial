package com.indicasta.fullstackfactorial.s3;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class S3Service {

    private final S3Client s3;

    public S3Service(S3Client s3Client) {
        this.s3 = s3Client;
    }

    public void putObjectIntoS3(String bucketName, String objectKey, byte[] file) {

        PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
        s3.putObject(putOb, RequestBody.fromBytes(file));
    }

    public byte[] getObjectFromS3(String bucketName, String objectKey) {

        GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .key(objectKey)
                .bucket(bucketName)
                .build();

        try {
            ResponseBytes<GetObjectResponse> response = s3.getObjectAsBytes(objectRequest);
            System.out.println("Successfully obtained bytes from an S3 object");
            return response.asByteArray();
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            return new byte[0];
        }
    }

}
