package com.neratama.api.blog.like;

import com.neratama.api.blog.article.Article;
import com.neratama.api.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "article_likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"article_id", "user_id"})
})
public class ArticleLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public ArticleLike() {
    }

    public ArticleLike(Long id, Article article, User user, LocalDateTime createdAt) {
        this.id = id;
        this.article = article;
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

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
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

    public static ArticleLikeBuilder builder() {
        return new ArticleLikeBuilder();
    }

    public static class ArticleLikeBuilder {
        private Long id;
        private Article article;
        private User user;
        private LocalDateTime createdAt;

        public ArticleLikeBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ArticleLikeBuilder article(Article article) {
            this.article = article;
            return this;
        }

        public ArticleLikeBuilder user(User user) {
            this.user = user;
            return this;
        }

        public ArticleLikeBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ArticleLike build() {
            return new ArticleLike(this.id, this.article, this.user, this.createdAt);
        }
    }
}
