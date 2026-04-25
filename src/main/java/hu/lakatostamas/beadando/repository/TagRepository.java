package hu.lakatostamas.beadando.repository;

import hu.lakatostamas.beadando.model.Tag;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TagRepository {

    private final Map<Long, Tag> entities = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Tag save(Tag tag) {
        if (tag.getId() == null) {
            tag.setId(idGenerator.getAndIncrement());
        }
        entities.put(tag.getId(), tag);
        return tag;
    }

    public Optional<Tag> findById(Long id) {
        return Optional.ofNullable(entities.get(id));
    }

    public List<Tag> findAll() {
        return new ArrayList<>(entities.values());
    }

    public boolean deleteById(Long id) {
        return entities.remove(id) != null;
    }

    public Optional<Tag> findByName(String name) {
        return entities.values().stream()
                .filter(tag -> tag.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}
