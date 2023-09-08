package io.bhimsur.sandbox.service;

import io.bhimsur.sandbox.config.StorageConfiguration;
import io.bhimsur.sandbox.dto.ObjectStorageDTO;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Service(value = CloudStorageService.SERVICE_NAME)
public class CloudStorageService implements StorageService {
    public static final String SERVICE_NAME = "googleCloudStorageService";
    @Autowired
    @Qualifier(StorageConfiguration.GCS_BEAN)
    private MinioClient minioClient;
    @Value("${storage.gcs.bucket}")
    private String bucket;

    @Override
    public ObjectStorageDTO getObject(String path) {
        try (GetObjectResponse object = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(path)
                .build())) {
            String contentType = object.headers().get(HttpHeaders.CONTENT_TYPE);
            return new ObjectStorageDTO(getObjectName(object.object()), getPreSignedUrl(path), contentType, IOUtils.toByteArray(object));
        } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ObjectStorageDTO storeObject(MultipartFile object, String directory) {
        try {
            byte[] bytes = object.getBytes();
            try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
                ObjectWriteResponse response = minioClient.putObject(PutObjectArgs.builder()
                        .stream(inputStream, bytes.length, -1)
                        .bucket(bucket)
                        .contentType(object.getContentType())
                        .object(directory + object.getOriginalFilename())
                        .build());
                return new ObjectStorageDTO(response.object(), getPreSignedUrl(response.object()), object.getContentType());
            }
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteObject(String path) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(path)
                    .build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String getPreSignedUrl(String path) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucket)
                    .object(path)
                    .expiry(5, TimeUnit.MINUTES)
                    .method(Method.GET)
                    .build());
        } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | XmlParserException |
                 ServerException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String getObjectName(String object) {
        String[] split = object.split("/");
        int length = split.length;
        return split[length - 1];
    }
}
