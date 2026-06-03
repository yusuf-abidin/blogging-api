package com.neratama.api.media;

import com.neratama.api.common.response.ApiResponse;
import com.neratama.api.media.dto.MediaResponse;
import com.neratama.api.security.UserPrincipal;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final MediaService mediaService;;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<MediaResponse>> uploadFile(
            @RequestParam("file")MultipartFile file,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Media uploadedMedia = mediaService.uploadMedia(file, userPrincipal.getUser());
        MediaResponse mediaResponse = new MediaResponse(uploadedMedia);
        return new ResponseEntity<>(ApiResponse.success("File berhasil diupload", mediaResponse), HttpStatus.CREATED);
    }

    @GetMapping("/view/{filename:.+}")
    public ResponseEntity<Resource> viewFile(@PathVariable String filename) {
        try{
            Path filePath = Paths.get("uploads").toAbsolutePath().normalize().resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .contentType(MediaType.IMAGE_PNG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
