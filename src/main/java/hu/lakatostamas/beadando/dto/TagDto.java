package hu.lakatostamas.beadando.dto;

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
public class TagDto {

    private Long id;

    @NotBlank(message = "A címke neve nem lehet üres")
    @Size(min = 2, max = 30, message = "A címke 2 és 30 karakter között kell legyen")
    private String name;

    @Size(max = 200, message = "A leírás maximum 200 karakter hosszú lehet")
    private String description;

    private Integer usageCount;

    @NotNull(message = "Az aktív státusz nem lehet null")
    private Boolean active;

    private Integer postCount; // Csak egy aggregált statisztika kimegy networkon, nem az egész Post objektumfa
}
