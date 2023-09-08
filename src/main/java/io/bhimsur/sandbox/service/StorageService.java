package io.bhimsur.sandbox.service;

import io.bhimsur.sandbox.dto.ObjectStorageDTO;
import org.springframework.web.multipart.MultipartFile;


public interface StorageService {
    ObjectStorageDTO getObject(String path);
    ObjectStorageDTO storeObject(MultipartFile object, String directory);
    void deleteObject(String path);
    String getPreSignedUrl(String path);
}
