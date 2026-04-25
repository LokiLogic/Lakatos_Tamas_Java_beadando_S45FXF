package hu.lakatostamas.beadando.service;

import hu.lakatostamas.beadando.exception.EntityNotFoundException;
import hu.lakatostamas.beadando.exception.IllegalOperationException;
import hu.lakatostamas.beadando.model.User;
import hu.lakatostamas.beadando.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User create(User user) {
        if (user.getId() != null && userRepository.findById(user.getId()).isPresent()) {
            throw new IllegalOperationException("A felhasználó már létezik ezzel az azonosítóval!");
        }
        return userRepository.save(user);
    }

    public User update(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setAge(updatedUser.getAge());
        existingUser.setActive(updatedUser.getActive());

        return userRepository.save(existingUser);
    }

    public void delete(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));

        if (!existingUser.getPosts().isEmpty() || !existingUser.getComments().isEmpty()) {
            throw new IllegalOperationException("A felhasználó nem törölhető, mert vannak posztjai vagy kommentjei. Először ezeket kell törölni.");
        }

        userRepository.deleteById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }
}
