package com.neratama.api.blog.tag;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String slug;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Tag() {
    }

    public Tag(Long id, String name, String slug, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (this.slug == null && this.name != null) {
            this.slug = this.name.toLowerCase().trim().replaceAll("[^a-z0-9\\s]", "").replaceAll("\\s+", "-");
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) && Objects.equals(name, tag.name) && Objects.equals(slug, tag.slug) && Objects.equals(createdAt, tag.createdAt) && Objects.equals(updatedAt, tag.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, slug, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public static TagBuilder builder() {
        return new TagBuilder();
    }

    public static class TagBuilder {
        private Long id;
        private String name;
        private String slug;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public TagBuilder() {
        }

        public TagBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TagBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TagBuilder slug(String slug) {
            this.slug = slug;
            return this;
        }

        public TagBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public TagBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Tag build() {
            return new Tag(this.id, this.name, this.slug, this.createdAt, this.updatedAt);
        }
    }
}
