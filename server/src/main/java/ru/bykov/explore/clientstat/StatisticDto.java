package ru.bykov.explore.clientstat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticDto {
    private String app;
    private String uri;
    private String ip;
}
