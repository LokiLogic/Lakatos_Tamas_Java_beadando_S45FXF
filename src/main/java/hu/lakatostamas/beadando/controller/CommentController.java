package hu.lakatostamas.beadando.controller;

import hu.lakatostamas.beadando.dto.CommentDto;
import hu.lakatostamas.beadando.model.Comment;
import hu.lakatostamas.beadando.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> create(@Valid @RequestBody CommentDto dto) {
        Comment comment = mapToEntity(dto);
        // A Service befogadja az objektumot és a két szülő ID-ját is:
        Comment savedComment = commentService.create(comment, dto.getPostId(), dto.getCommenterId());
        return new ResponseEntity<>(mapToDto(savedComment), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> update(@PathVariable Long id, @Valid @RequestBody CommentDto dto) {
        Comment comment = mapToEntity(dto);
        Comment updatedComment = commentService.update(id, comment);
        return ResponseEntity.ok(mapToDto(updatedComment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAll(
            @RequestParam(required = false) Long postId,
            @RequestParam(required = false) Long commenterId) {

        List<Comment> comments = commentService.findAll();

        if (postId != null) {
            comments = comments.stream()
                    .filter(c -> c.getPost() != null && c.getPost().getId().equals(postId))
                    .collect(Collectors.toList());
        }
        if (commenterId != null) {
            comments = comments.stream()
                    .filter(c -> c.getCommenter() != null && c.getCommenter().getId().equals(commenterId))
                    .collect(Collectors.toList());
        }

        List<CommentDto> responseDtos = comments.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }

    // --- Mapper metódusok ---
    private Comment mapToEntity(CommentDto dto) {
        Comment comment = new Comment();
        comment.setId(dto.getId());
        comment.setText(dto.getText());
        comment.setUpvotes(dto.getUpvotes() != null ? dto.getUpvotes() : 0);
        comment.setApproved(dto.getApproved() != null ? dto.getApproved() : false);
        return comment;
    }

    private CommentDto mapToDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getUpvotes(),
                comment.getApproved(),
                comment.getPost().getId(),
                comment.getCommenter().getId(),
                comment.getCommenter().getUsername()
        );
    }
}
