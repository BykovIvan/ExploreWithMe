package ru.bykov.explore.model;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Location {
    private Long id;
    private Float lat;
    private Float lon;
}
