package com.neratama.api.blog.article;

import com.neratama.api.blog.article.dto.ArticleResponse;
import com.neratama.api.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ArticleResponse>>> getAllPublishedArticles() {
        List<ArticleResponse> articles = articleService.getAllPublishedArticles();
        return ResponseEntity.ok(ApiResponse.success("Berhasil mengambil data", articles));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<ArticleResponse>> getArticleDetails(@PathVariable String slug) {
        ArticleResponse article = articleService.getArticleBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success("Berhasil mengambil data", article));
    }
}
