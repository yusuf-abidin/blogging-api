package com.neratama.api.blog.comment;

import com.neratama.api.blog.article.Article;
import com.neratama.api.blog.article.ArticleRepository;
import com.neratama.api.blog.comment.dto.CommentResponse;
import com.neratama.api.blog.comment.dto.CreateCommentRequest;
import com.neratama.api.blog.comment.dto.UpdateCommentRequest;
import com.neratama.api.common.exception.ResourceNotFound;
import com.neratama.api.common.exception.UnauthorizedException;
import com.neratama.api.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public CommentService(CommentRepository commentRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }

    @Transactional
    public CommentResponse createComment(CreateCommentRequest request, User user) {
        Article article = articleRepository.findById(request.getArticleId())
                .orElseThrow(() -> new ResourceNotFound("Artikel dengan ID: " + request.getArticleId() + " tidak ditemukan"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .article(article)
                .user(user)
                .build();

        if (request.getParentId() != null) {
            Comment parentComment = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFound("Komentar dengan ID: " + request.getParentId() + " tidak ditemukan"));

            comment.setParent(parentComment);
        }

        Comment savedComment = commentRepository.save(comment);
        return new CommentResponse(savedComment);
    }

    @Transactional
    public List<CommentResponse> getCommentsByArticle(Long articleId) {
        return commentRepository.findByArticleIdAndParentIsNullOrderByCreatedAtAsc(articleId)
                .stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, UpdateCommentRequest request, User currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFound("Komentar dengan ID: " + commentId + " tidak ditemukan"));

        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("Anda tidak memiliki izin untuk mengedit komentar ini");
        }

        comment.setContent(request.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return new CommentResponse(updatedComment);
    }
}
