package ru.bykov.explore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.bykov.explore.utils.StateOfEventAndReq;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "events", schema = "public")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String annotation;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private Category category;
    @Column(name = "confirmed_requests")
    private Long confirmedRequests;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private User initiator;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private Location location;
    @Column(nullable = false, columnDefinition = "false")
    private Boolean paid;
    @Column(name = "participant_limit", nullable = false, columnDefinition = "0")
    private Long participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation", nullable = false, columnDefinition = "true")
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private StateOfEventAndReq state;
    private String title;

}
