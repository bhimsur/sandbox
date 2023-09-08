package io.bhimsur.sandbox.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfiguration {
    public static final String GCS_BEAN = "gcsBean";

    @Value("${storage.gcs.endpoint}")
    private String gcsEndpoint;
    @Value("${storage.gcs.key.secret}")
    private String gcsSecret;
    @Value("${storage.gcs.key.access}")
    private String gcsAccess;

    @Bean(name = GCS_BEAN)
    public MinioClient gcsClient() {
        return MinioClient.builder()
                .endpoint(gcsEndpoint)
                .credentials(gcsAccess, gcsSecret)
                .build();
    }
}
