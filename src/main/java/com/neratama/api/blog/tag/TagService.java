package com.neratama.api.blog.tag;

import com.neratama.api.blog.tag.dto.TagRequest;
import com.neratama.api.common.exception.BadRequestException;
import com.neratama.api.common.exception.ResourceNotFound;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional
    public Tag createTag(TagRequest request) {
        if (tagRepository.existsByName(request.getName())) {
            throw new BadRequestException("Tag dengan nama " + request.getName() + " sudah ada");
        }

        Tag tag = Tag.builder()
                .name(request.getName())
                .build();

        return tagRepository.save(tag);
    }

    @Transactional(readOnly = true)
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Transactional
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Tag dengan ID: " + id + " tidak ditemukan"));

        tagRepository.delete(tag);
    }

    @Transactional
    public Tag updateTag(Long id, TagRequest request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Tag dengan ID: " + id + " tidak ditemukan"));

        if (!tag.getName().equalsIgnoreCase(request.getName()) && tagRepository.existsByName(request.getName())) {
            throw new BadRequestException("Tag dengan nama " + request.getName() + " sudah ada");
        }

        String newSlug = request.getName().toLowerCase().trim()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-");

        tag.setName(request.getName());
        tag.setSlug(newSlug);

        return tagRepository.save(tag);
    }
}
