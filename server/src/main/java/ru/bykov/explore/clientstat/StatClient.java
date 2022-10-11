package ru.bykov.explore.clientstat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.bykov.explore.clientstat.client.BaseClient;

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

//    public void getStat() {
//        get("");
//    }

}
