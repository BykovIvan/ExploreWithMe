package ru.bykov.explore.model;

import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

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
@Table(name = "compilations", schema = "public")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "id", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private List<Event> events;
    @Column(nullable = false, columnDefinition = "false")
    private Boolean pinned;
    private String title;
}
