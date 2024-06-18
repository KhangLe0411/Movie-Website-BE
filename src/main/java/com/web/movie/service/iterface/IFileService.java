package com.web.movie.service.iterface;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    String uploadFile(MultipartFile file);
    void deleteFile(String url);
}
