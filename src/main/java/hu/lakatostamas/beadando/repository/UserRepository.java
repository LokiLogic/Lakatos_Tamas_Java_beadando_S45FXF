package hu.lakatostamas.beadando.repository;

import hu.lakatostamas.beadando.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {

    private final Map<Long, User> entities = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
        }
        entities.put(user.getId(), user);
        return user;
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entities.get(id));
    }

    public List<User> findAll() {
        return new ArrayList<>(entities.values());
    }

    public boolean deleteById(Long id) {
        return entities.remove(id) != null;
    }

    public Optional<User> findByUsername(String username) {
        return entities.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }
}
