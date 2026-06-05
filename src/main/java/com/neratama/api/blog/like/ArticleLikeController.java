package com.neratama.api.blog.like;

import com.neratama.api.common.response.ApiResponse;
import com.neratama.api.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class ArticleLikeController {

    private final ArticleLikeService articleLikeService;

    public ArticleLikeController(ArticleLikeService articleLikeService) {
        this.articleLikeService = articleLikeService;
    }

    @PreAuthorize("hasAuthority('VERIFIED')")
    @PostMapping("/article/{articleId}")
    public ResponseEntity<ApiResponse<String>> toggleArticleLike(
            @PathVariable Long articleId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String message = articleLikeService.toogleLike(articleId, userPrincipal.getUser());

        return ResponseEntity.ok(ApiResponse.success(message, null));
    }

    @GetMapping("/article/{articleId}/count")
    public ResponseEntity<ApiResponse<Long>> getArticleLikeCount(@PathVariable Long articleId) {
        long count = articleLikeService.getLikeCount(articleId);

        return ResponseEntity.ok(ApiResponse.success("Berhasil mengambil total suka", count));
    }
}
