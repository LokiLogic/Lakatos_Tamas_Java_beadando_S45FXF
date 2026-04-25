package hu.lakatostamas.beadando.service;

import hu.lakatostamas.beadando.exception.EntityNotFoundException;
import hu.lakatostamas.beadando.exception.IllegalOperationException;
import hu.lakatostamas.beadando.model.Tag;
import hu.lakatostamas.beadando.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Tag create(Tag tag) {
        if (tagRepository.findByName(tag.getName()).isPresent()) {
            throw new IllegalOperationException("Már létezik ilyen nevű címke!");
        }
        return tagRepository.save(tag);
    }

    public Tag update(Long id, Tag updatedTag) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Tag.class, id));

        existingTag.setName(updatedTag.getName());
        existingTag.setDescription(updatedTag.getDescription());
        existingTag.setActive(updatedTag.getActive());
        
        return tagRepository.save(existingTag);
    }

    public void delete(Long id) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Tag.class, id));

        if (!existingTag.getPosts().isEmpty()) {
            throw new IllegalOperationException("A címke nem törölhető, mert vannak hozzárendelt posztok.");
        }

        tagRepository.deleteById(id);
    }

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public Tag findById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Tag.class, id));
    }
}
