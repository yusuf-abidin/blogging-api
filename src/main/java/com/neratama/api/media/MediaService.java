package com.neratama.api.media;

import com.neratama.api.media.storage.StorageService;
import com.neratama.api.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MediaService {

    private final MediaRepository mediaRepository;
    private final StorageService storageService;

    public MediaService(MediaRepository mediaRepository, StorageService storageService) {
        this.mediaRepository = mediaRepository;
        this.storageService = storageService;
    }

    @Transactional
    public Media uploadMedia(MultipartFile file, User user) {
        String fileUrl = storageService.storeFile(file);

        Media media = Media.builder()
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .fileUrl(fileUrl)
                .user(user)
                .build();

        return mediaRepository.save(media);
    }
}
