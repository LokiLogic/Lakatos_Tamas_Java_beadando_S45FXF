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
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    // 1. alaptípus tároló osztály: Long
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 2. alaptípus tároló osztály: String
    @NotBlank(message = "A komment szövege nem lehet üres")
    @Size(max = 500, message = "A komment maximum 500 karakter lehet")
    private String text;

    // 3. alaptípus: Integer
    @NotNull(message = "A pontszám nem lehet null")
    @Min(value = 0, message = "A pontszám nem lehet negatív")
    private Integer upvotes = 0;

    // 4. alaptípus: Boolean
    @NotNull(message = "A jóváhagyott státusz megadása kötelező")
    private Boolean approved = false;

    // Kapcsolatok (Több-Egy) a Post felé
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @NotNull(message = "A poszt ID-t kötelező megadni a kommenthez")
    private Post post;

    // Kapcsolatok (Több-Egy) a User felé
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "A szerzőt kötelező megadni a kommenthez")
    private User commenter;

    // A collection feltételnek megfelelően adunk hozzá egy egyszerű Listát
    @ElementCollection
    @CollectionTable(name = "comment_flags", joinColumns = @JoinColumn(name = "comment_id"))
    @Column(name = "flag")
    private List<String> flags = new ArrayList<>();
}
