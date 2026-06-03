package com.neratama.api.media;

import com.neratama.api.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "media")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_type", nullable = false, length = 100)
    private String fileType;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Media() {
    }

    public Media(Long id, String fileName, String fileType, Long fileSize, String fileUrl, User user, LocalDateTime createdAt) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.fileUrl = fileUrl;
        this.user = user;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static MediaBuilder builder() {
        return new MediaBuilder();
    }

    public static class MediaBuilder {

        private Long id;
        private String fileName;
        private String fileType;
        private Long fileSize;
        private String fileUrl;
        private User user;
        private LocalDateTime createdAt;

        public MediaBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MediaBuilder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public MediaBuilder fileType(String fileType) {
            this.fileType = fileType;
            return this;
        }

        public MediaBuilder fileSize(Long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        public MediaBuilder fileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
            return this;
        }

        public MediaBuilder user(User user) {
            this.user = user;
            return this;
        }

        public MediaBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Media build() {
            return new Media(this.id, this.fileName, this.fileType, this.fileSize, this.fileUrl, this.user, this.createdAt);
        }
    }
}
