package hu.lakatostamas.beadando.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(message = "A felhasználónév nem lehet üres")
    @Size(min = 3, max = 50, message = "A felhasználónév 3 és 50 karakter között kell legyen")
    private String username;

    @NotBlank(message = "Az email cím megadása kötelező")
    @Email(message = "Érvénytelen email formátum")
    private String email;

    @NotNull(message = "Az életkor megadása kötelező")
    @Min(value = 18, message = "A felhasználónak legalább 18 évesnek kell lennie")
    private Integer age;

    @NotNull(message = "Az aktív státusz nem lehet null")
    private Boolean active;
}
