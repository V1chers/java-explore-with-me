package ru.practicum.ewm.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewm.exception.InternalServerError;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }

    protected ResponseEntity<Object> get(String path) {
        return get(path, null, null);
    }

    protected ResponseEntity<Object> get(String path, long userId) {
        return get(path, userId, null);
    }

    protected ResponseEntity<Object> get(String path, Long userId, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, userId, parameters, null);
    }

    protected <T> ResponseEntity<Object> post(String path, T body) {
        return post(path, null, null, body);
    }

    protected <T> ResponseEntity<Object> post(String path, long userId, T body) {
        return post(path, userId, null, body);
    }

    protected <T> ResponseEntity<Object> post(String path, Long userId, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, userId, parameters, body);
    }

    protected <T> ResponseEntity<Object> put(String path, long userId, T body) {
        return put(path, userId, null, body);
    }

    protected <T> ResponseEntity<Object> put(String path, long userId, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.PUT, path, userId, parameters, body);
    }

    protected <T> ResponseEntity<Object> patch(String path, T body) {
        return patch(path, null, null, body);
    }

    protected <T> ResponseEntity<Object> patch(String path, long userId) {
        return patch(path, userId, null, null);
    }

    protected <T> ResponseEntity<Object> patch(String path, long userId, T body) {
        return patch(path, userId, null, body);
    }

    protected <T> ResponseEntity<Object> patch(String path, Long userId, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.PATCH, path, userId, parameters, body);
    }

    protected ResponseEntity<Object> delete(String path) {
        return delete(path, null, null);
    }

    protected ResponseEntity<Object> delete(String path, long userId) {
        return delete(path, userId, null);
    }

    protected ResponseEntity<Object> delete(String path, Long userId, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.DELETE, path, userId, parameters, null);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, Long userId, @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders(userId));

        Map<String, Collection<Object>> parameterCollections = findCollections(parameters);
        String filledPath = fillParameterCollections(parameterCollections, path);


        ResponseEntity<Object> ewmServerResponse;
        try {
            if (parameters != null) {
                ewmServerResponse = rest.exchange(filledPath, method, requestEntity, Object.class, parameters);
            } else {
                ewmServerResponse = rest.exchange(filledPath, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode responseBody = mapper.readTree(e.getResponseBodyAsString());
                return ResponseEntity.status(e.getStatusCode()).body(responseBody);
            } catch (JsonProcessingException jme) {
                log.error("Произошла ошибка при создании json объекта");
                throw new InternalServerError("Произошла ошибка при создании json объекта");
            }
        }
        return prepareGatewayResponse(ewmServerResponse);
    }

    private HttpHeaders defaultHeaders(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (userId != null) {
            headers.set("X-Sharer-User-Id", String.valueOf(userId));
        }
        return headers;
    }

    private Map<String, Collection<Object>> findCollections(Map<String, Object> parameters) {
        if (parameters == null) {
            return null;
        }

        Map<String, Collection<Object>> collectedParameters = new HashMap<>();

        parameters.forEach((key, value) -> {
            if (value instanceof Collection) {
                collectedParameters.put(key, (Collection<Object>) value);
            }
        });

        return collectedParameters;
    }

    private String fillParameterCollections(Map<String, Collection<Object>> parameterCollections, String path) {
        if (parameterCollections == null) {
            return path;
        }

        String pathToChange = path;

        for (Map.Entry<String, Collection<Object>> collectionEntry : parameterCollections.entrySet()) {
            String key = collectionEntry.getKey();

            String partToReplace = key + "={" + key + "}";
            StringBuilder replacementPart = new StringBuilder();

            for (Object value : collectionEntry.getValue()) {
                if (replacementPart.isEmpty()) {
                    replacementPart.append(key).append("=").append(value);
                    continue;
                }

                replacementPart.append("&").append(key).append("=").append(value);
            }

            pathToChange = pathToChange.replace(partToReplace, replacementPart);
        }

        return pathToChange;
    }
}

