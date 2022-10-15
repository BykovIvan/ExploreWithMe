package ru.bykov.explore.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.bykov.explore.utils.EventState;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

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
    private String annotation;
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category")
    private Category category;
    @Column(name = "confirmed_requests")
    private Long confirmedRequests;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @NotNull
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
    private Boolean paid;
    private Long participantLimit;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    @NotNull
    @Enumerated(EnumType.STRING)
    private EventState state;
    private String title;
    private Boolean CommentModeration;
//    @OneToMany()                   //Комментарии можно будет смотреть только по отдельной ссылке для каждого события
//    private List<Comment> comments;
}
