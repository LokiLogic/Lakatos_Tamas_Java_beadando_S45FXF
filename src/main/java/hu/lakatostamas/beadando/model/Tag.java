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
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    // 1. alaptípus tároló osztály: Long
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 2. alaptípus tároló osztály: String
    @NotBlank(message = "A címke neve nem lehet üres")
    @Size(min = 2, max = 30, message = "A címke 2 és 30 karakter között kell legyen")
    private String name;

    @Size(max = 200, message = "A leírás maximum 200 karakter hosszú lehet")
    private String description;

    // 3. alaptípus: Integer
    @NotNull(message = "A használati gyakoriság nem lehet null")
    @Min(value = 0, message = "A használati gyakoriság nem lehet negatív")
    private Integer usageCount = 0;

    // 4. alaptípus: Boolean
    @NotNull(message = "Az aktív státusz nem lehet null")
    private Boolean active = true;

    // Collection (Több-Több kapcsolat a Post felé - a Post a birtokos oldal)
    @ManyToMany(mappedBy = "tags")
    private List<Post> posts = new ArrayList<>();
}
