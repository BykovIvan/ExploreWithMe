package ru.bykov.explore.model;

import lombok.*;
import ru.bykov.explore.utils.StateOfEvent;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "events",
        schema = "public")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String annotation;
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;
    private Long confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.EAGER)
    private User initiator;
    @ManyToOne(fetch = FetchType.EAGER)
    private Location location;
    @Column(nullable = false, columnDefinition = "false")
    private Boolean paid;
    @Column(nullable = false, columnDefinition = "0")
    private Long participantLimit;
    private LocalDateTime publishedOn;
    @Column(nullable = false, columnDefinition = "true")
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private StateOfEvent state;
    private String title;

}
