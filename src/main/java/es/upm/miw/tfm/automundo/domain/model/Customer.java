package es.upm.miw.tfm.automundo.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer {
    @NotBlank
    private String id;
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

    public String getCompletedName() {
        StringBuilder completedName = new StringBuilder();
        completedName.append(name).append(" ").append(surName);
        if(secondSurName != null && !secondSurName.isEmpty()){
            completedName.append(" ").append(secondSurName);
        }
        return completedName.toString();
    }
}
