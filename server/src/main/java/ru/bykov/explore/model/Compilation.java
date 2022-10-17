package ru.bykov.explore.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "compilations", schema = "public")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany()
    private List<Event> events;
    @Column(nullable = false, columnDefinition = "false")
    private Boolean pinned;                 //Закреплена ли подборка на главной странице сайта
    private String title;                   //Заголовок подборки
}
