package com.neratama.api.media.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String storeFile(MultipartFile file);
}
