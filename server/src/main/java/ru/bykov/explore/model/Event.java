package ru.bykov.explore.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.bykov.explore.utils.EventState;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
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
    @NotNull
    @NotBlank
    @Size(min = 1, max = 2000)
    private String annotation;
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category")
    private Category category;
    @NotNull
    @Column(name = "confirmed_requests")
    private Long confirmedRequests;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 7000)
    private String description;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "initiator")
    private User initiator;
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location")
    private Location location;
    @NotNull
    private Boolean paid;
    @NotNull
    @PositiveOrZero
    private Long participantLimit;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @NotNull
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private EventState state;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 120)
    private String title;
    @NotNull
    private Boolean commentModeration;
}
