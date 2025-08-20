package ru.practicum.ewm.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewm.exception.models.ClientExceptionResult;

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

    private static <R> ResponseEntity<R> prepareGatewayResponse(ResponseEntity<R> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }

    protected <R> ResponseEntity<R> get(String path, Class<R> responseType) {
        return get(path, null, responseType);
    }

    protected <R> ResponseEntity<R> get(String path,
                                        @Nullable Map<String, Object> parameters,
                                        Class<R> responseType) {
        return makeAndSendRequest(HttpMethod.GET, path, parameters, null, responseType);
    }

    protected <T, R> ResponseEntity<R> post(String path, T body, Class<R> responseType) {
        return post(path, null, body, responseType);
    }

    protected <T, R> ResponseEntity<R> post(String path,
                                            @Nullable Map<String, Object> parameters,
                                            T body, Class<R> responseType) {
        return makeAndSendRequest(HttpMethod.POST, path, parameters, body, responseType);
    }

    protected <T, R> ResponseEntity<R> put(String path, T body, Class<R> responseType) {
        return put(path, null, body, responseType);
    }

    protected <T, R> ResponseEntity<R> put(String path,
                                           @Nullable Map<String, Object> parameters,
                                           T body, Class<R> responseType) {
        return makeAndSendRequest(HttpMethod.PUT, path, parameters, body, responseType);
    }

    protected <R> ResponseEntity<R> patch(String path, Class<R> responseType) {
        return patch(path, null, null, responseType);
    }

    protected <T, R> ResponseEntity<R> patch(String path, T body, Class<R> responseType) {
        return patch(path, null, body, responseType);
    }

    protected <T, R> ResponseEntity<R> patch(String path,
                                             @Nullable Map<String, Object> parameters,
                                             T body, Class<R> responseType) {
        return makeAndSendRequest(HttpMethod.PATCH, path, parameters, body, responseType);
    }

    protected <R> ResponseEntity<R> delete(String path, Class<R> responseType) {
        return delete(path, null, responseType);
    }

    protected <R> ResponseEntity<R> delete(String path,
                                           @Nullable Map<String, Object> parameters,
                                           Class<R> responseType) {
        return makeAndSendRequest(HttpMethod.DELETE, path, parameters, null, responseType);
    }

    private <T, R> ResponseEntity<R> makeAndSendRequest(HttpMethod method, String path,
                                                        @Nullable Map<String, Object> parameters, @Nullable T body,
                                                        Class<R> responseType) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        Map<String, Collection<Object>> parameterCollections = findCollections(parameters);
        String filledPath = fillParameterCollections(parameterCollections, path);

        ResponseEntity<R> ewmServerResponse;
        try {
            if (parameters != null) {
                ewmServerResponse = rest.exchange(filledPath, method, requestEntity, responseType, parameters);
            } else {
                ewmServerResponse = rest.exchange(filledPath, method, requestEntity, responseType);
            }
        } catch (HttpStatusCodeException e) {
            throw new ClientExceptionResult(e);
        }
        return prepareGatewayResponse(ewmServerResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
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

