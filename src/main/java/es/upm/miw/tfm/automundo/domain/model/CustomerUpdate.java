package es.upm.miw.tfm.automundo.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class CustomerUpdate {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastVisitDate;
    private String phone;
    private String mobilePhone;
    private String address;
    private String email;
    private String name;
    private String surName;
    private String secondSurName;
}
