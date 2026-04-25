package hu.lakatostamas.beadando.repository;

import hu.lakatostamas.beadando.model.Post;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class PostRepository {

    private final Map<Long, Post> entities = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Post save(Post post) {
        if (post.getId() == null) {
            post.setId(idGenerator.getAndIncrement());
        }
        entities.put(post.getId(), post);
        return post;
    }

    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(entities.get(id));
    }

    public List<Post> findAll() {
        return new ArrayList<>(entities.values());
    }

    public boolean deleteById(Long id) {
        return entities.remove(id) != null;
    }

    public List<Post> findAllByAuthorId(Long authorId) {
        return entities.values().stream()
                .filter(post -> post.getAuthor() != null && post.getAuthor().getId().equals(authorId))
                .collect(Collectors.toList());
    }
}
