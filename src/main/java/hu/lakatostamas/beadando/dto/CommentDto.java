package hu.lakatostamas.beadando.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;

    @NotBlank(message = "A komment szövege nem lehet üres")
    @Size(max = 500, message = "A komment maximum 500 karakter lehet")
    private String text;

    private Integer upvotes;

    @NotNull(message = "A jóváhagyott státusz megadása kötelező")
    private Boolean approved;

    @NotNull(message = "A poszt ID-t kötelező megadni a kommenthez")
    private Long postId;

    @NotNull(message = "A szerzőt kötelező megadni a kommenthez")
    private Long commenterId;

    private String commenterName; // Csak egy szöveges info a megjelenítéshez
}
