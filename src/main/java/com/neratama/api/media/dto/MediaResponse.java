package com.neratama.api.media.dto;

import com.neratama.api.media.Media;
import com.neratama.api.user.dto.UserResponse;

import java.time.LocalDateTime;

public class MediaResponse {
    private Long id;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String fileUrl;
    private UserResponse user;
    private LocalDateTime createdAt;

    public MediaResponse(Media media) {
        this.id = media.getId();
        this.fileName = media.getFileName();
        this.fileType = media.getFileType();
        this.fileSize = media.getFileSize();
        this.fileUrl = media.getFileUrl();
        this.user = new UserResponse(media.getUser());
        this.createdAt = media.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
