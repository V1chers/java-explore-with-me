package ru.practicum.ewm.main.client.admin.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.client.BaseClient;
import ru.practicum.ewm.main.dto.user.CreateUserDto;

import java.util.List;
import java.util.Map;

@Service
public class AdminUserClient extends BaseClient {

    private static final String API_PREFIX = "/admin/users";

    @Autowired
    public AdminUserClient(@Value("${ewm.server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<List> getUsers(List<Integer> ids, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "ids", ids,
                "from", from,
                "size", size
        );

        return get("?ids={ids}&from={from}&size={size}", parameters, List.class);
    }

    public ResponseEntity<Object> createUser(CreateUserDto userDto) {
        return post("", userDto, Object.class);
    }

    public ResponseEntity<Void> deleteUser(Integer userId) {
        Map<String, Object> parameters = Map.of(
                "userId", userId
        );

        return delete("/{userId}", parameters, Void.class);
    }
}
