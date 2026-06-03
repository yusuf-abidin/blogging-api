package com.neratama.api.media;

import com.neratama.api.common.exception.BadRequestException;
import com.neratama.api.common.exception.ResourceNotFound;
import com.neratama.api.media.storage.StorageService;
import com.neratama.api.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class MediaService {

    private final MediaRepository mediaRepository;
    private final StorageService storageService;
    private final Path fileStorageLocation;

    public MediaService(MediaRepository mediaRepository, StorageService storageService, @Value("${app.upload.dir:uploads}") String uploadDir) {
        this.mediaRepository = mediaRepository;
        this.storageService = storageService;
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
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

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = fileStorageLocation.resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new ResourceNotFound("File tidak ditemukan: " + fileName);
            }
            return resource;
        }catch (MalformedURLException e) {
            throw new BadRequestException("URL file tidak valid: " + fileName);
        }
    }

    public MediaType detectContentType(String filename) {
        try {
            Path filePath = fileStorageLocation.resolve(filename).normalize();
            String contentType = Files.probeContentType(filePath);

            if (contentType == null) {
                return MediaType.APPLICATION_OCTET_STREAM;
            }

            return MediaType.parseMediaType(contentType);
        } catch (IOException ex) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
