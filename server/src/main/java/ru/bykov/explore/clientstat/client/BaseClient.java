package ru.bykov.explore.clientstat.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;
import ru.bykov.explore.clientstat.ViewStats;

import java.util.Map;

@RequiredArgsConstructor
public class BaseClient {
    protected final RestTemplate rest;

    protected <T> ResponseEntity<Object> post(String path, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, body);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body);
        ResponseEntity<Object> statServerResponse = rest.exchange(path, method, requestEntity, Object.class);
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

    protected ResponseEntity<ViewStats[]> get(String path, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequestViews(path, parameters);
    }

    private ResponseEntity<ViewStats[]> makeAndSendRequestViews(String path, Map<String, Object> parameters) {
        ResponseEntity<ViewStats[]> statServerResponse = rest.getForEntity(path, ViewStats[].class, parameters);
        return prepareGatewayResponseView(statServerResponse);
    }

    private static ResponseEntity<ViewStats[]> prepareGatewayResponseView(ResponseEntity<ViewStats[]> response) {
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
