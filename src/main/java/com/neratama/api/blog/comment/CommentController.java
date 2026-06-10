package com.neratama.api.blog.comment;

import com.neratama.api.blog.comment.dto.CommentResponse;
import com.neratama.api.blog.comment.dto.CreateCommentRequest;
import com.neratama.api.blog.comment.dto.UpdateCommentRequest;
import com.neratama.api.common.response.ApiResponse;
import com.neratama.api.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("hasAuthority('VERIFIED')")
    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> addComment(
            @Valid @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        CommentResponse response = commentService.createComment(request, userPrincipal.getUser());
        return new ResponseEntity<>(ApiResponse.success("Komentar berhasil ditambahkan", response), HttpStatus.CREATED);
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getArticleComments(@PathVariable Long articleId) {
        List<CommentResponse> comments = commentService.getCommentsByArticle(articleId);
        return ResponseEntity.ok(ApiResponse.success("Berhasil mengambil data komentar", comments));
    }

    @PreAuthorize("hasAuthority('VERIFIED')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCommentRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        CommentResponse response = commentService.updateComment(id, request, userPrincipal.getUser());

        return ResponseEntity.ok(ApiResponse.success("Komentar berhasil diperbarui", response));
    }
}
