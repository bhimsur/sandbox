package io.bhimsur.sandbox.controller;

import io.bhimsur.sandbox.dto.ObjectStorageDTO;
import io.bhimsur.sandbox.service.CloudStorageService;
import io.bhimsur.sandbox.service.StorageService;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/gcs")
public class CloudStorageController implements StorageController {
    @Autowired
    @Qualifier(CloudStorageService.SERVICE_NAME)
    private StorageService storageService;

    @Override
    public ResponseEntity<ObjectStorageDTO> storeObject(MultipartFile object, String folder) {
        return new ResponseEntity<>(storageService.storeObject(object, folder), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> getObject(String path, HttpServletResponse httpServletResponse) {
        ObjectStorageDTO object = storageService.getObject(path);
        httpServletResponse.setContentType(object.getContentType());
        httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + object.getName());
        try (InputStream inputStream = new ByteArrayInputStream(object.getObject())) {
            IOUtils.copy(inputStream, httpServletResponse.getOutputStream());
            httpServletResponse.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteObject(String path) {
        storageService.deleteObject(path);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
