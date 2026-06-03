package com.neratama.api.blog.like;

import com.neratama.api.blog.article.Article;
import com.neratama.api.blog.article.ArticleRepository;
import com.neratama.api.common.exception.ResourceNotFound;
import com.neratama.api.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ArticleLikeService {

    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleRepository articleRepository;

    public ArticleLikeService(ArticleLikeRepository articleLikeRepository, ArticleRepository articleRepository) {
        this.articleLikeRepository = articleLikeRepository;
        this.articleRepository = articleRepository;
    }

    @Transactional
    public String toogleLike(Long articleId, User user) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFound("Artikel yang ingin disukai tidak ditemukan"));

        Optional<ArticleLike> existingLike = articleLikeRepository.findByArticleIdAndUserId(articleId, user.getId());

        if (existingLike.isPresent()) {
            articleLikeRepository.delete(existingLike.get());
            return "Batal menyukai artikel ini";
        } else {
            ArticleLike like = ArticleLike.builder()
                    .article(article)
                    .user(user)
                    .build();
            articleLikeRepository.save(like);
            return "Berhasil menyukai artikel ini";
        }
    }

    @Transactional(readOnly = true)
    public long getLikeCount(Long articleId) {
        if (!articleRepository.existsById(articleId)) {
            throw new ResourceNotFound("Artikel tidak ditemukan");
        }
        return articleLikeRepository.countByArticleId(articleId);
    }
}
