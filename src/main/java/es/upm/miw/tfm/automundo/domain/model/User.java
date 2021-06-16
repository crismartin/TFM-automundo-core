package es.upm.miw.tfm.automundo.domain.model;

import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User {
    @NotBlank
    private String userName;
    @NotBlank
    private String realName;
    private String surName;
    private String secondSurName;
    @NotBlank
    private String dni;
    private String password;
    private Role role;
    private LocalDateTime registrationDate;
}
