package com.neratama.api.blog.tag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TagRequest {

    @NotBlank(message = "Nama tag tidak boleh kosong")
    @Size(max = 50, message = "Nama tag maksimal 50 karakter")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
