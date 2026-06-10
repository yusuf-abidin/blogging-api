package com.neratama.api.blog.article.dto;

import com.neratama.api.blog.article.Article;
import com.neratama.api.blog.tag.Tag;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class ArticleResponse {

    private Long id;
    private String title;
    private String slug;
    private String content;
    private String summary;
    private String coverImage;
    private String status;
    private Long viewCount;
    private LocalDateTime publishedAt;
    private String authorName;
    private Set<String> tags;
    private LocalDateTime createdAt;

    public ArticleResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.slug = article.getSlug();
        this.content = article.getContent();
        this.summary = article.getSummary();
        this.coverImage = article.getCoverImage();
        this.status = article.getStatus().name();
        this.publishedAt = article.getPublishedAt();
        this.viewCount = article.getViewCount();
        this.authorName = article.getUser().getFullName();
        this.tags = article.getTags().stream().map(Tag::getName).collect(Collectors.toSet());
        this.createdAt = article.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
