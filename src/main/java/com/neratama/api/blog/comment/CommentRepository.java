package com.neratama.api.blog.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByArticleIdAndParentIsNullOrderByCreatedAtAsc(Long articleId);
}
