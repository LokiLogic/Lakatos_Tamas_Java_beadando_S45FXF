package hu.lakatostamas.beadando.repository;

import hu.lakatostamas.beadando.model.Comment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class CommentRepository {

    private final Map<Long, Comment> entities = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            comment.setId(idGenerator.getAndIncrement());
        }
        entities.put(comment.getId(), comment);
        return comment;
    }

    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(entities.get(id));
    }

    public List<Comment> findAll() {
        return new ArrayList<>(entities.values());
    }

    public boolean deleteById(Long id) {
        return entities.remove(id) != null;
    }

    public List<Comment> findAllByPostId(Long postId) {
        return entities.values().stream()
                .filter(comment -> comment.getPost() != null && comment.getPost().getId().equals(postId))
                .collect(Collectors.toList());
    }
}
