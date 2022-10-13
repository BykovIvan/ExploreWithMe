package ru.bykov.explore.model;

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
@Table(name = "requests", schema = "public")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime created;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event", referencedColumnName = "id")
    private Event event;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "requester", referencedColumnName = "id")
    private User requester;
    @Enumerated(EnumType.STRING)
    private StateOfEventAndReq status;
}
