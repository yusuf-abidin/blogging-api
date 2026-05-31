package com.neratama.api.blog.article;

import com.neratama.api.blog.article.dto.ArticleResponse;
import com.neratama.api.blog.article.dto.CreateArticleRequest;
import com.neratama.api.blog.tag.Tag;
import com.neratama.api.blog.tag.TagRepository;
import com.neratama.api.common.exception.BadRequestException;
import com.neratama.api.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

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
}
