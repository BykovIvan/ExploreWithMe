package ru.bykov.explore.clientstat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.bykov.explore.clientstat.client.BaseClient;

import java.util.Map;

@Service
public class StatClient extends BaseClient {

    private static final String API_PREFIX = "/";

    @Autowired
    public StatClient(@Value("${explore-with-me-statistics.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public void createStat(StatisticDto statisticDto) {
        post("hit", statisticDto);
    }

    public ResponseEntity<Object> getViewsOfEvent(String app, String uri) {
        Map<String, Object> parameters = Map.of(
                "app", app,
                "uri", uri
        );
        return get("count" + "?app={app}&uri={uri}", parameters);
    }

    public ResponseEntity<Object> getStatByParam(String start, String end, String[] uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        return get("stats" + "?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

}
