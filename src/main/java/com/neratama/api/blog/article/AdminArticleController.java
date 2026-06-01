package com.neratama.api.blog.article;

import com.neratama.api.blog.article.dto.ArticleResponse;
import com.neratama.api.blog.article.dto.CreateArticleRequest;
import com.neratama.api.common.response.ApiResponse;
import com.neratama.api.security.UserPrincipal;
import com.neratama.api.user.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/articles")
public class AdminArticleController {

    private final ArticleService articleService;

    public AdminArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ArticleResponse>> createDraft(@Valid @RequestBody CreateArticleRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        User author = userPrincipal.getUser();

        ArticleResponse responseData = articleService.createDraftArticle(request, author);

        return new ResponseEntity<>(ApiResponse.success("Draft Artikel berhasil dibuat", responseData), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ArticleResponse>> updateArticleStatus(
            @PathVariable Long id,
            @RequestParam ArticleStatus status) {
        ArticleResponse articleResponse = articleService.updateArticleStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Status artikel berhasil diperbarui", articleResponse));
    }
}
