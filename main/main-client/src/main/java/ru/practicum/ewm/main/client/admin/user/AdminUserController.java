package ru.practicum.ewm.main.client.admin.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.user.CreateUserDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
@SuppressWarnings({"unused", "rawtypes"})
public class AdminUserController {

    private final AdminUserClient adminUserClient;

    @GetMapping
    public ResponseEntity<List> getUsers(@RequestParam(required = false) List<Integer> ids,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size) {
        return adminUserClient.getUsers(ids, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody CreateUserDto userDto) {
        return adminUserClient.createUser(userDto);
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        return adminUserClient.deleteUser(userId);
    }
}
