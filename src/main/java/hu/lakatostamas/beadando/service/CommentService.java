package hu.lakatostamas.beadando.service;

import hu.lakatostamas.beadando.exception.EntityNotFoundException;
import hu.lakatostamas.beadando.exception.IllegalOperationException;
import hu.lakatostamas.beadando.model.Comment;
import hu.lakatostamas.beadando.model.Post;
import hu.lakatostamas.beadando.model.User;
import hu.lakatostamas.beadando.repository.CommentRepository;
import hu.lakatostamas.beadando.repository.PostRepository;
import hu.lakatostamas.beadando.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Comment create(Comment comment, Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(Post.class, postId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, userId));

        comment.setPost(post);
        comment.setCommenter(user);

        Comment savedComment = commentRepository.save(comment);

        // Szinkronizáció a modelleken (Egy-Több kapcsolatok fenntartása a InMemory DB végett)
        post.getComments().add(savedComment);
        user.getComments().add(savedComment);

        return savedComment;
    }

    public Comment update(Long id, Comment updatedComment) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Comment.class, id));

        existingComment.setText(updatedComment.getText());
        existingComment.setUpvotes(updatedComment.getUpvotes());
        existingComment.setApproved(updatedComment.getApproved());

        return commentRepository.save(existingComment);
    }

    public void delete(Long id) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Comment.class, id));

        // Letisztítani a kapcsolatokat, mert InMemory. A JVM memóriaszivárgások ellen és az objektum fa tisztán tartására!
        existingComment.getPost().getComments().remove(existingComment);
        existingComment.getCommenter().getComments().remove(existingComment);

        commentRepository.deleteById(id);
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Comment.class, id));
    }
}
