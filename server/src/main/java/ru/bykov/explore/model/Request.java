package ru.bykov.explore.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.bykov.explore.utils.EventState;
import ru.bykov.explore.utils.RequestState;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "requests", schema = "public")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event", referencedColumnName = "id")
    private Event event;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "requester", referencedColumnName = "id")
    private User requester;
    @Enumerated(EnumType.STRING)
    private RequestState status;
}
