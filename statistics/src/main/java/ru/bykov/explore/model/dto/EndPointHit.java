package ru.bykov.explore.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndPointHit {
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
