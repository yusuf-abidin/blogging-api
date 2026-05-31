package com.neratama.api.blog.tag;

import com.neratama.api.blog.tag.dto.TagRequest;
import com.neratama.api.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Tag>>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return ResponseEntity.ok(ApiResponse.success("Berhasil mengambil data", tags));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Tag>> createTag(@Valid @RequestBody
    TagRequest request) {
        Tag createdTag = tagService.createTag(request);
        return new ResponseEntity<>(ApiResponse.success("Tag berhasil dibuat", createdTag),HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);

        return ResponseEntity.ok(ApiResponse.success("Tag berhasil dihapus", null));
    }

}
