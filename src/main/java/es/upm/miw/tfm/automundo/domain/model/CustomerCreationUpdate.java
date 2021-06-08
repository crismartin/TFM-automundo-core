package es.upm.miw.tfm.automundo.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CustomerCreationUpdate {
    @NotBlank
    private String identificationId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registrationDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastVisitDate;
    private String phone;
    private String mobilePhone;
    private String address;
    private String email;
    private String name;
    private String surName;
    private String secondSurName;

    public void doDefault() {
        if (Objects.isNull(registrationDate)) {
            this.registrationDate = LocalDateTime.now();
        }
        if (Objects.isNull(lastVisitDate)) {
            this.lastVisitDate = LocalDateTime.now();
        }
    }
}
