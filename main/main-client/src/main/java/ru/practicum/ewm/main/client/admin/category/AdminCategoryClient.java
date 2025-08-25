package ru.practicum.ewm.main.client.admin.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.client.BaseClient;
import ru.practicum.ewm.main.dto.category.CategoryDto;

import java.util.Map;

@Service
public class AdminCategoryClient extends BaseClient {
    private static final String API_PREFIX = "/admin/categories";

    @Autowired
    public AdminCategoryClient(@Value("${ewm.server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createCategory(CategoryDto categoryDto) {
        return post("", categoryDto, Object.class);
    }

    public ResponseEntity<Void> deleteCategory(Integer catId) {
        Map<String, Object> parameters = Map.of(
                "catId", catId
        );
        return delete("/{catId}", parameters, void.class);
    }

    public ResponseEntity<Object> changeCategory(Integer catId, CategoryDto categoryDto) {
        Map<String, Object> parameters = Map.of(
                "catId", catId
        );
        return patch("/{catId}", parameters, categoryDto, Object.class);
    }
}
