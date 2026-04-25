package hu.lakatostamas.beadando.service;

import hu.lakatostamas.beadando.exception.EntityNotFoundException;
import hu.lakatostamas.beadando.exception.IllegalOperationException;
import hu.lakatostamas.beadando.model.Post;
import hu.lakatostamas.beadando.model.Tag;
import hu.lakatostamas.beadando.model.User;
import hu.lakatostamas.beadando.repository.PostRepository;
import hu.lakatostamas.beadando.repository.TagRepository;
import hu.lakatostamas.beadando.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public Post create(Post post, Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, authorId));

        post.setAuthor(author);
        Post savedPost = postRepository.save(post);

        // Kézi kapcsolat szinkronizáció - InMemory db-nél kötelező!
        author.getPosts().add(savedPost);

        return savedPost;
    }

    public Post update(Long id, Post updatedPost) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Post.class, id));

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setPublished(updatedPost.getPublished());
        existingPost.setViewCount(updatedPost.getViewCount());

        return postRepository.save(existingPost);
    }

    public void delete(Long id) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Post.class, id));

        if (!existingPost.getComments().isEmpty()) {
            throw new IllegalOperationException("A poszt nem törölhető, mert vannak rajta kommentek.");
        }

        // Törlés előtt kikeressük a posztot a User és a Tag(ek) listáiból, hogy a memória is tiszta maradjon
        existingPost.getAuthor().getPosts().remove(existingPost);
        for (Tag tag : existingPost.getTags()) {
            tag.getPosts().remove(existingPost);
            // Használati gyakoriság csökkentése
            tag.setUsageCount(Math.max(0, tag.getUsageCount() - 1));
        }

        postRepository.deleteById(id);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Post.class, id));
    }

    // Több-Több kapcsolat karbantartása
    public Post addTagToPost(Long postId, Long tagId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(Post.class, postId));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException(Tag.class, tagId));

        if (!post.getTags().contains(tag)) {
            post.getTags().add(tag);
            tag.getPosts().add(post);
            tag.setUsageCount(tag.getUsageCount() + 1);
        } else {
            throw new IllegalOperationException("Ez a címke már hozzá van adva ehhez a poszthoz.");
        }

        return post;
    }
}
