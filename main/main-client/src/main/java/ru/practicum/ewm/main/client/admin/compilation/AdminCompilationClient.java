package ru.practicum.ewm.main.client.admin.compilation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.client.BaseClient;
import ru.practicum.ewm.main.dto.compilation.CreateCompilationDto;
import ru.practicum.ewm.main.dto.compilation.UpdatedCompilationDto;

import java.util.Map;

@Service
public class AdminCompilationClient extends BaseClient {

    private static final String API_PREFIX = "/admin/compilations";

    @Autowired
    public AdminCompilationClient(@Value("${ewm.server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createCompilation(CreateCompilationDto createCompilationDto) {
        return post("", createCompilationDto, Object.class);
    }

    public ResponseEntity<Void> deleteCompilation(int compId) {
        Map<String, Object> parameters = Map.of(
                "compId", compId
        );

        return delete("/{compId}", parameters, Void.class);
    }

    public ResponseEntity<Object> updateCompilation(int compId, UpdatedCompilationDto compilationDto) {
        Map<String, Object> parameters = Map.of(
                "compId", compId
        );

        return patch("/{compId}", parameters, compilationDto, Object.class);
    }
}
