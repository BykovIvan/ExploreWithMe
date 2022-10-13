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

    //для post views через массив, работает
    protected <T> ResponseEntity<Object> post(String path, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, body);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, @Nullable T body) {
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

//test
//    protected ViewStatsList get(String path, @Nullable Map<String, Object> parameters) {
//        return makeAndSendRequestViews(path, parameters);
//    }
//
//    private ViewStatsList makeAndSendRequestViews(String path, @Nullable Map<String, Object> parameters) {
//        return rest.getForObject(path, ViewStatsList.class, parameters);
//    }


    //для get views через массив, работает
    protected ResponseEntity<ViewStats[]> get(String path, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequestViews(path, parameters);
    }

    private ResponseEntity<ViewStats[]> makeAndSendRequestViews(String path, @Nullable Map<String, Object> parameters) {
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


//    protected ResponseEntity<Object> get(String path) {
//        return get(path, null);
//    }
//
//    protected ResponseEntity<Object> get(String path, @Nullable Map<String, Object> parameters) {
//        return makeAndSendRequest(HttpMethod.GET, path, parameters, null);
//    }

//    protected <T> ResponseEntity<Object> post(String path, T body) {
//        return post(path, null, body);
//    }
//
//    protected <T> ResponseEntity<Object> post(String path, @Nullable Map<String, Object> parameters, T body) {
//        return makeAndSendRequest(HttpMethod.POST, path, parameters, body);
//    }
//
//    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, @Nullable Map<String, Object> parameters, @Nullable T body) {
//        HttpEntity<T> requestEntity = new HttpEntity<>(body);
//        ResponseEntity<Object> statServerResponse;
//        try {
//            if (parameters != null) {
//                statServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
//            } else {
//                statServerResponse = rest.exchange(path, method, requestEntity, Object.class);
//            }
//        } catch (HttpStatusCodeException e) {
//            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
//        }
//        return prepareGatewayResponse(statServerResponse);
//    }
//
//    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
//        if (response.getStatusCode().is2xxSuccessful()) {
//            return response;
//        }
//        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
//        if (response.hasBody()) {
//            return responseBuilder.body(response.getBody());
//        }
//        return responseBuilder.build();
//    }


}
