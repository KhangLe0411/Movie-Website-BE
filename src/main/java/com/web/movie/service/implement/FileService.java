package com.web.movie.service.implement;

import com.web.movie.config.MinioConfig;
import com.web.movie.service.iterface.IFileService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService implements IFileService {
    private static final String BUCKET_NAME = "film";
    private final MinioConfig config;
    @Override
    public String uploadFile(MultipartFile file) {
        var filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        var key = UUID.randomUUID() + "." +filenameExtension;

        try{
            MinioClient minioClient = config.minioClient();
            minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(key)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
            );

            return config.getEndpoint() + "/" + BUCKET_NAME + "/" + key;
        }catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An Exception occurred while uploading the file"
            );
        }
    }

    @Override
    public void deleteFile(String url) {
        String key = url.substring(url.lastIndexOf("/") + 1);
        try {
            MinioClient minioClient = config.minioClient();
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(key)
                            .build()
            );
        }catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An Exception occurred while deleting the file"
            );
        }
    }
}
