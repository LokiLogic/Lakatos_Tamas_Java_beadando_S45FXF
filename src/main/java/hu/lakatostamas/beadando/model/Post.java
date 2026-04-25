package hu.lakatostamas.beadando.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    // 1. alaptípus tároló osztály: Long
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 2. alaptípus tároló osztály: String
    @NotBlank(message = "A cím megadása kötelező")
    @Size(min = 5, max = 100, message = "A cím legalább 5, legfeljebb 100 karakter lehet")
    private String title;

    @NotBlank(message = "A tartalom nem lehet üres")
    @Lob // Ez hosszú szövegeknél javasolt
    private String content;

    // 3. alaptípus: Integer
    @NotNull(message = "A megtekintések száma nem lehet null")
    @Min(value = 0, message = "A megtekintések száma nem lehet negatív")
    private Integer viewCount = 0;

    // 4. alaptípus: Boolean
    @NotNull(message = "A publikációs státusz megadása kötelező")
    private Boolean published = false;

    // Kapcsolatok (Több-Egy)
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    @NotNull(message = "A szerzőt meg kell adni a poszthoz")
    private User author;

    // Collection kapcsolata - Egy-Több (Comment)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // Collection kapcsolata - Több-Több (Tag)
    @ManyToMany
    @JoinTable(
        name = "post_tags",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();
}
