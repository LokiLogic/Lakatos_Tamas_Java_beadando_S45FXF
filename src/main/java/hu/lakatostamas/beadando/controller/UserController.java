package hu.lakatostamas.beadando.controller;

import hu.lakatostamas.beadando.dto.UserDto;
import hu.lakatostamas.beadando.model.User;
import hu.lakatostamas.beadando.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto userDto) {
        User user = mapToEntity(userDto);
        User savedUser = userService.create(user);
        return new ResponseEntity<>(mapToDto(savedUser), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        User user = mapToEntity(userDto);
        User updatedUser = userService.update(id, user);
        return ResponseEntity.ok(mapToDto(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Boolean active) {
        
        List<User> users = userService.findAll();

        // Szűrés a kontroller rétegben (Stream API segítségével)
        if (username != null && !username.isBlank()) {
            users = users.stream()
                    .filter(u -> u.getUsername().toLowerCase().contains(username.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (active != null) {
            users = users.stream()
                    .filter(u -> u.getActive().equals(active))
                    .collect(Collectors.toList());
        }

        List<UserDto> responseDtos = users.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }

    // --- Mapper metódusok ---
    private User mapToEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        user.setActive(dto.getActive() != null ? dto.getActive() : true);
        return user;
    }

    private UserDto mapToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAge(),
                user.getActive()
        );
    }
}
