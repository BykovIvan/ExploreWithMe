package ru.bykov.explore.clientstat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.bykov.explore.clientstat.client.BaseClient;
import ru.bykov.explore.model.Event;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class StatClient extends BaseClient {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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

    public ResponseEntity<ViewStats[]> getStatByParam(String start, String end, String[] uris, Boolean unique) {
        String encStart = encodeValue(start);
        String encEnd = encodeValue(end);
        Map<String, Object> parameters = Map.of(
                "start", encStart,
                "end", encEnd,
                "uris", uris,
                "unique", unique
        );
        return get("stats" + "?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

    public Long getViews(Event event) {
        ResponseEntity<ViewStats[]> str = getStatByParam(event.getCreatedOn().minusMinutes(1).format(formatter),
                LocalDateTime.now().plusMinutes(1).format(formatter),
                new String[]{"/events/" + event.getId()},
                false);
        ViewStats[] viewStats = str.getBody();
        if (viewStats != null && viewStats.length > 0) {
            return viewStats[0].getHits();
        }
        return 0L;
    }

    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
