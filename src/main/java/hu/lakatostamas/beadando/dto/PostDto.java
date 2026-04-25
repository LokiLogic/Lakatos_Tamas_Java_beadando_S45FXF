package hu.lakatostamas.beadando.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long id;

    @NotBlank(message = "A cím megadása kötelező")
    @Size(min = 5, max = 100, message = "A cím legalább 5, legfeljebb 100 karakter lehet")
    private String title;

    @NotBlank(message = "A tartalom nem lehet üres")
    private String content;

    private Integer viewCount;

    @NotNull(message = "A publikációs státusz megadása kötelező")
    private Boolean published;

    // Csak a hivatkozott azonosítók vagy a lényeges szöveges nevek mennek ki a hálózaton:
    @NotNull(message = "A szerzőt meg kell adni a poszthoz")
    private Long authorId;

    private String authorName; // read-only tulajdonságként

    // Kapcsolt kollekciók adatai (körkörös referenciák elkerülésével)
    private List<String> tagNames;
    private Integer commentCount;
}
