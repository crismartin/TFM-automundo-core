package es.upm.miw.tfm.automundo.infrastructure.mongodb.entities;

import es.upm.miw.tfm.automundo.domain.model.User;
import lombok.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class UserEntity {
    @Id
    @Generated
    private String id;
    @NonNull
    @Indexed(unique = true)
    private String userName;
    @NonNull
    private String realName;
    private String surName;
    private String secondSurName;
    @NonNull
    @Indexed(unique = true)
    private String dni;
    private String password;
    private Role role;
    private LocalDateTime registrationDate;

    public User toUser() {
        User user = new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }
}

