package ru.bykov.explore.clientstat.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.bykov.explore.utils.ViewsDto;

import javax.validation.ValidationException;
import java.util.Map;

@RequiredArgsConstructor
public class BaseClient {
    protected final RestTemplate rest;

    protected ResponseEntity<Object> get(String path) {
        return get(path, null);
    }

    protected ResponseEntity<Object> get(String path, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, parameters, null);
    }

    protected <T> ResponseEntity<Object> post(String path, T body) {
        return post(path, null, body);
    }

    protected <T> ResponseEntity<Object> post(String path, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, parameters, body);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body);
        ResponseEntity<Object> statServerResponse;
        try {
            if (parameters != null) {
                statServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                statServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(statServerResponse);
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

    //Подсчет views для каждого события
    protected ResponseEntity<ViewsDto> getViews(String path, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequestViews(HttpMethod.GET, path, parameters, null);
    }

    private <T> ResponseEntity<ViewsDto> makeAndSendRequestViews(HttpMethod method, String path, @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body);
        ResponseEntity<ViewsDto> statServerResponse;
        try {
            if (parameters != null) {
                statServerResponse = rest.exchange(path, method, requestEntity, ViewsDto.class, parameters);
            } else {
                statServerResponse = rest.exchange(path, method, requestEntity, ViewsDto.class);
            }
        } catch (HttpStatusCodeException e) {
            throw new ValidationException();
        }
        return prepareGatewayResponseViews(statServerResponse);
    }

    private static ResponseEntity<ViewsDto> prepareGatewayResponseViews(ResponseEntity<ViewsDto> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }
        return responseBuilder.build();
    }

}
