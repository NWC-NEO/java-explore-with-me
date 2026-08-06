package ru.practicum.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.util.List;

@Component
public class StatsClient {

    private final RestClient restClient;

    public StatsClient(
            @Value("${stats-server.url:http://localhost:9090}") String serverUrl,
            RestClient.Builder builder
    ) {
        this.restClient = builder
                .baseUrl(serverUrl)
                .build();
    }

    public void saveHit(EndpointHitDto hitDto) {
        restClient.post()
                .uri("/hit")
                .body(hitDto)
                .retrieve()
                .toBodilessEntity();
    }

    public ResponseEntity<List<ViewStatsDto>> getStats(
            String start,
            String end,
            List<String> uris,
            boolean unique
    ) {
        return restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/stats")
                            .queryParam("start", start)
                            .queryParam("end", end)
                            .queryParam("unique", unique);
                    if (uris != null && !uris.isEmpty()) {
                        uriBuilder.queryParam("uris", uris);
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<ViewStatsDto>>() {
                });
    }
}
