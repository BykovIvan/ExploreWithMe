package ru.bykov.explore.model;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Compilation {
    private Long id;
    private List<Event> events;
    private Boolean pinned;
    private String title;
}
