package es.upm.miw.tfm.automundo.domain.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class TechnicianUpdate {
    private String ssNumber;
    private String mobile;
    private String name;
    private String surName;
    private String secondSurName;
    private Boolean active;
}
