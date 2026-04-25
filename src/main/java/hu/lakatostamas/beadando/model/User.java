package hu.lakatostamas.beadando.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
@Table(name = "users") // A 'user' név egyes adatbázisokban védett lehet, így többnyire 'users'
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    // 1. alaptípus tároló osztály: Long
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 2. alaptípus tároló osztály: String
    @NotBlank(message = "A felhasználónév nem lehet üres")
    @Size(min = 3, max = 50, message = "A felhasználónév 3 és 50 karakter között kell legyen")
    private String username;

    @NotBlank(message = "Az email cím megadása kötelező")
    @Email(message = "Érvénytelen email formátum")
    private String email;

    // 3. alaptípus: Integer
    @NotNull(message = "Az életkor megadása kötelező")
    @Min(value = 18, message = "A felhasználónak legalább 18 évesnek kell lennie")
    private Integer age;

    // 4. alaptípus: Boolean
    @NotNull(message = "Az aktív státusz nem lehet null")
    private Boolean active = true;

    // Collection (Egy-Több kapcsolat a Post felé)
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    // Opcionális: Egy-Több kapcsolat a Comment felé is
    @OneToMany(mappedBy = "commenter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
}
