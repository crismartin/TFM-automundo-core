package es.upm.miw.tfm.automundo.infrastructure.mongodb.entities;

import es.upm.miw.tfm.automundo.domain.model.Technician;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document
public class TechnicianEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    private String identificationId;
    private String ssNumber;
    private LocalDateTime registrationDate;
    private LocalDateTime leaveDate;
    private String mobile;
    private String name;
    private String surName;
    private String secondSurName;
    private Boolean active;

    public Technician toTechnician() {
        Technician technician = new Technician();
        BeanUtils.copyProperties(this, technician);
        return technician;
    }
}
