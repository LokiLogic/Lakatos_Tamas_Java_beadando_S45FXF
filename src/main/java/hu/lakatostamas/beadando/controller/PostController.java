package hu.lakatostamas.beadando.controller;

import hu.lakatostamas.beadando.dto.PostDto;
import hu.lakatostamas.beadando.model.Post;
import hu.lakatostamas.beadando.model.Tag;
import hu.lakatostamas.beadando.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> create(@Valid @RequestBody PostDto postDto) {
        Post post = mapToEntity(postDto);
        Post savedPost = postService.create(post, postDto.getAuthorId());
        return new ResponseEntity<>(mapToDto(savedPost), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> update(@PathVariable Long id, @Valid @RequestBody PostDto postDto) {
        Post post = mapToEntity(postDto);
        Post updatedPost = postService.update(id, post);
        return ResponseEntity.ok(mapToDto(updatedPost));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getAll(
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) String tag) {
        
        List<Post> posts = postService.findAll();

        // Szűrés a kontroller rétegben Stream + feltételek alapján
        if (authorId != null) {
            posts = posts.stream()
                    .filter(p -> p.getAuthor() != null && p.getAuthor().getId().equals(authorId))
                    .collect(Collectors.toList());
        }
        if (tag != null && !tag.isBlank()) {
            posts = posts.stream()
                    .filter(p -> p.getTags().stream().anyMatch(t -> t.getName().equalsIgnoreCase(tag)))
                    .collect(Collectors.toList());
        }

        List<PostDto> responseDtos = posts.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }

    // Speciális N:M kapcsolat frissítő végpont: tag hozzárendelése a poszthoz
    @PostMapping("/{postId}/tags/{tagId}")
    public ResponseEntity<PostDto> addTagToPost(@PathVariable Long postId, @PathVariable Long tagId) {
        Post updatedPost = postService.addTagToPost(postId, tagId);
        return ResponseEntity.ok(mapToDto(updatedPost));
    }

    // --- Mapper metódusok ---
    private Post mapToEntity(PostDto dto) {
        Post post = new Post();
        post.setId(dto.getId());
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setViewCount(dto.getViewCount() != null ? dto.getViewCount() : 0);
        post.setPublished(dto.getPublished() != null ? dto.getPublished() : false);
        return post;
    }

    private PostDto mapToDto(Post post) {
        List<String> tagNames = post.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
                
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getViewCount(),
                post.getPublished(),
                post.getAuthor().getId(),
                post.getAuthor().getUsername(), // read-only DTO info!
                tagNames,
                post.getComments().size()
        );
    }
}
