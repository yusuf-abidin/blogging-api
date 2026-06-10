package com.neratama.api.blog.article.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class UpdateArticleRequest {

    @NotBlank(message = "Judul artikel tidak boleh kosong")
    @Size(max = 255, message = "Judul artikel tidak boleh lebih dari 255 karakter")
    private String title;

    @NotBlank(message = "Konten artikel tidak boleh kosong")
    private String content;

    private String summary;
    private String coverImage;

    @NotEmpty(message = "Artikel minimal harus memiliki satu tag")
    private Set<Long> tagIds;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Set<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<Long> tagIds) {
        this.tagIds = tagIds;
    }
}
