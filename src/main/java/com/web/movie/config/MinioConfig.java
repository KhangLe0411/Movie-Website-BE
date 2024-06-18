package com.web.movie.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {
    private String endpoint;
    private Integer port;
    private String accessKey;
    private String secretKey;
    private boolean secure;

    public MinioClient minioClient(){
        try{
            MinioClient client = MinioClient.builder()
                    .credentials(accessKey, secretKey)
                    .endpoint(endpoint, port, secure)
                    .build();
            return client;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
