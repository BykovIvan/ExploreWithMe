package ru.bykov.explore.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
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
    @JoinColumn(name = "category")
    private Category category;
    @Column(name = "confirmed_requests")
    private Long confirmedRequests;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "initiator")
    private User initiator;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location")
    private Location location;
    @Column(nullable = false, columnDefinition = "false")
    private Boolean paid;
    @Column(name = "participant_limit", nullable = false, columnDefinition = "0")
    private Long participantLimit;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation", nullable = false, columnDefinition = "true")
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private StateOfEventAndReq state;
    private String title;

}
