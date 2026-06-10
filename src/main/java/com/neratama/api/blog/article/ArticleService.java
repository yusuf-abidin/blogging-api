package com.neratama.api.blog.article;

import com.neratama.api.blog.article.dto.ArticleResponse;
import com.neratama.api.blog.article.dto.CreateArticleRequest;
import com.neratama.api.blog.article.dto.UpdateArticleRequest;
import com.neratama.api.blog.tag.Tag;
import com.neratama.api.blog.tag.TagRepository;
import com.neratama.api.common.exception.BadRequestException;
import com.neratama.api.common.exception.ResourceNotFound;
import com.neratama.api.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;

    public ArticleService(ArticleRepository articleRepository, TagRepository tagRepository) {
        this.articleRepository = articleRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional
    public ArticleResponse createDraftArticle(CreateArticleRequest request, User author) {
        String generatedSlug = request.getTitle().toLowerCase().trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-");

        if (articleRepository.existsBySlug(generatedSlug)) {
            throw new BadRequestException("Judul artikel sudah pernah digunakan!, buat judul yang lebih unik");
        }

        List<Tag> foundTags = tagRepository.findAllById(request.getTagIds());
        if (foundTags.size() != request.getTagIds().size()) {
            throw new BadRequestException("Salah satu / beberapa tag yang diberikan tidak ditemukan");
        }

        Article article = Article.builder()
                .title(request.getTitle())
                .slug(generatedSlug)
                .content(request.getContent())
                .summary(request.getSummary())
                .coverImage(request.getCoverImage())
                .status(ArticleStatus.DRAFT)
                .user(author)
                .tags(new HashSet<>(foundTags))
                .build();

        Article savedArticle = articleRepository.save(article);
        return new ArticleResponse(savedArticle);
    }

    @Transactional(readOnly = true)
    public List<ArticleResponse> getAllPublishedArticles() {
        return articleRepository.findAllByStatusOrderByCreatedAtDesc(ArticleStatus.PUBLISHED)
                .stream()
                .map(ArticleResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ArticleResponse getArticleBySlug(String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFound("Artikel dengan url tersebut tidak ditemukan"));

        if (article.getStatus() != ArticleStatus.PUBLISHED) {
            throw new BadRequestException("Artikel ini belum diterbitkan");
        }

        article.setViewCount(article.getViewCount() + 1);
        Article updatedArticle = articleRepository.save(article);

        return new ArticleResponse(updatedArticle);
    }

    @Transactional
    public ArticleResponse updateArticleStatus(Long id, ArticleStatus status) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Artikel dengan ID:" + id + " tidak ditemukan"));

        article.setStatus(status);
        if (status == ArticleStatus.PUBLISHED && article.getPublishedAt() == null) {
            article.setPublishedAt(LocalDateTime.now());
        }
        Article updatedArticle = articleRepository.save(article);

        return new ArticleResponse(updatedArticle);
    }

    @Transactional
    public ArticleResponse updateArticle(Long id, UpdateArticleRequest request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Artikel dengan ID:" + id + " tidak ditemukan"));

        List<Tag> foundTags = tagRepository.findAllById(request.getTagIds());
        if (foundTags.size() != request.getTagIds().size()) {
            throw new BadRequestException("Salah satu / beberapa tag yang diberikan tidak ditemukan");
        }

        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setSummary(request.getSummary());
        article.setCoverImage(request.getCoverImage());
        article.setTags(new HashSet<>(foundTags));

        Article updatedArticle = articleRepository.save(article);
        return new ArticleResponse(updatedArticle);
    }
}
