package com.neratama.api.media.storage;

import com.neratama.api.common.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class LocalStorageService implements StorageService {

    private final Path fileStorageLocation;
    private final String baseUrl;

    private static final List<String> ALLOWED_EXTENSIONS = List.of("image/jpg", "image/jpeg", "image/png", "image/gif");
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;

    public LocalStorageService(
            @Value("${app.upload.dir:uploads}") String uploadDir,
            @Value("${app.base-url:http://localhost:8080/api}") String baseUrl) {

        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.baseUrl = baseUrl;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Gagal membuat direktori untuk menyimpan berkas.", ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Gagal mengunggah berkas kosong");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("Ukuran berkas melebihi batas maksimum, " + file.getSize());
        }

        String contentType = file.getContentType();
        if (!ALLOWED_EXTENSIONS.contains(contentType)) {
            throw new BadRequestException("Format berkas tidak diizinkan");
        }

        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String sanitizedFileName = UUID.randomUUID().toString() + fileExtension;

        try {
            Path targetLocation = this.fileStorageLocation.resolve(sanitizedFileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return baseUrl + "/media/view/" + sanitizedFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Gagal menyimpan berkas. Silakan coba lagi.", ex);
        }
    }
}
