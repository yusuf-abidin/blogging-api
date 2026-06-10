package com.neratama.api.blog.comment.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateCommentRequest {
    @NotBlank(message = "Isi komentar tidak boleh kosong")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
