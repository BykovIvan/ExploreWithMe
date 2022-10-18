package ru.bykov.explore.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndPointHit {
    private Long id;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 255)
    private String app;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 255)
    private String uri;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 64)
    private String ip;
    private String timestamp;
}
