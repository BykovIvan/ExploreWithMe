package ru.bykov.explore.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotNull
    @Column(nullable = false, columnDefinition = "false")
    private Boolean pinned;                 //Закреплена ли подборка на главной странице сайта
    @NotNull
    @NotBlank
    @Size(min = 1, max = 120)
    private String title;                   //Заголовок подборки
}
