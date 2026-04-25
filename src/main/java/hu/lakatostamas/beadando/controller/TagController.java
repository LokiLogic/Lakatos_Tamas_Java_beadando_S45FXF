package hu.lakatostamas.beadando.controller;

import hu.lakatostamas.beadando.dto.TagDto;
import hu.lakatostamas.beadando.model.Tag;
import hu.lakatostamas.beadando.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    public ResponseEntity<TagDto> create(@Valid @RequestBody TagDto tagDto) {
        Tag tag = mapToEntity(tagDto);
        Tag savedTag = tagService.create(tag);
        return new ResponseEntity<>(mapToDto(savedTag), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagDto> update(@PathVariable Long id, @Valid @RequestBody TagDto tagDto) {
        Tag tag = mapToEntity(tagDto);
        Tag updatedTag = tagService.update(id, tag);
        return ResponseEntity.ok(mapToDto(updatedTag));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TagDto>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minUsageCount) {
        
        List<Tag> tags = tagService.findAll();

        if (name != null && !name.isBlank()) {
            tags = tags.stream()
                    .filter(t -> t.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (minUsageCount != null) {
            tags = tags.stream()
                    .filter(t -> t.getUsageCount() >= minUsageCount)
                    .collect(Collectors.toList());
        }

        List<TagDto> responseDtos = tags.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }

    // --- Mapper metódusok ---
    private Tag mapToEntity(TagDto dto) {
        Tag tag = new Tag();
        tag.setId(dto.getId());
        tag.setName(dto.getName());
        tag.setDescription(dto.getDescription());
        tag.setUsageCount(dto.getUsageCount() != null ? dto.getUsageCount() : 0);
        tag.setActive(dto.getActive() != null ? dto.getActive() : true);
        return tag;
    }

    private TagDto mapToDto(Tag tag) {
        return new TagDto(
                tag.getId(),
                tag.getName(),
                tag.getDescription(),
                tag.getUsageCount(),
                tag.getActive(),
                tag.getPosts().size() // Hány poszthoz van csatolva! (M:N aggregátum)
        );
    }
}
