package com.neratama.api.blog.article;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findBySlug(String slug);

    List<Article> findAllByStatusOrderByCreatedAtDesc(ArticleStatus status);

    boolean existsBySlug(String slug);
}
