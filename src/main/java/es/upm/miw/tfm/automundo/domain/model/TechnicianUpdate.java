package es.upm.miw.tfm.automundo.domain.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TechnicianUpdate {
    private String ssNumber;
    private String mobile;
    private String name;
    private String surName;
    private String secondSurName;
    private Boolean active;
}
