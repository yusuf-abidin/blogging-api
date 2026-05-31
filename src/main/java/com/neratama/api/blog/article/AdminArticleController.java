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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
