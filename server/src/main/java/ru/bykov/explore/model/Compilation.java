package ru.bykov.explore.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "compilations",
        schema = "public")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @OneToMany()
    //может лучше Set хранить, тогда не будет повторений
    private List<Event> events;
    @NotNull
    private Boolean pinned;
    @NotNull
    @NotBlank
    private String title;
}
