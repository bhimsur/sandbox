package io.bhimsur.sandbox.controller;

import io.bhimsur.sandbox.dto.ObjectStorageDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface StorageController {
    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ObjectStorageDTO> storeObject(MultipartFile object, String folder);

    @GetMapping
    ResponseEntity<Void> getObject(@RequestParam("path") String path, HttpServletResponse httpServletResponse);

    @DeleteMapping
    ResponseEntity<Void> deleteObject(@RequestParam("path") String path);
}
