package es.upm.miw.tfm.automundo.domain.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TechnicianCreation {
    @NotBlank
    private String identificationId;
    private String ssNumber;
    private String mobile;
    private String name;
    private String surName;
    private String secondSurName;
}
