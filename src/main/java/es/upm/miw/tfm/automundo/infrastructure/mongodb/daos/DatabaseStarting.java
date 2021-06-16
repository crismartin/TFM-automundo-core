package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos;

import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.synchronous.UserDao;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.Role;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.UserEntity;
import org.apache.logging.log4j.LogManager;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.synchronous.CustomerDao;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DatabaseStarting {

    private UserDao userDao;

    private static final String ADMIN = "admin";
    private static final String USERNAME = "9";
    private static final String PASSWORD = "9";
    private static final String DNI = "00000000A";

    private CustomerDao customerDao;

    private static final String RANDOM_IDENTIFICATION_ID = "11111111-A";
    private static final String RANDOM_PHONE = "666666666";
    private static final String RANDOM_MOBILE_PHONE = "888888888";
    private static final String RANDOM_ADDRESS = "C/ Falsa 123, Madrid";
    private static final String RANDOM_EMAIL = "cliente1@gmail.com";
    private static final String RANDOM_NAME = "Pedro";
    private static final String RANDOM_SUR_NAME = "Pérez";
    private static final String RANDOM_SECOND_SUR_NAME = "Gutiérrez";

    @Autowired
    public DatabaseStarting(UserDao userDao, CustomerDao customerDao) {
        this.userDao = userDao;
        this.customerDao = customerDao;
        this.initialize();
    }

    void initialize() {
        LogManager.getLogger(this.getClass()).warn("------- Finding Admin -----------");
        if (this.userDao.findByRoleIn(List.of(Role.ADMIN)).isEmpty()) {
            UserEntity user = UserEntity.builder().userName(USERNAME).realName(ADMIN)
                    .surName(ADMIN).secondSurName(ADMIN).dni(DNI)
                    .password(new BCryptPasswordEncoder().encode(PASSWORD))
                    .role(Role.ADMIN).registrationDate(LocalDateTime.now()).build();
            this.userDao.save(user);
            LogManager.getLogger(this.getClass()).warn("------- Created Admin -----------");
        }
        if (this.customerDao.findByIdentificationId(RANDOM_IDENTIFICATION_ID).isEmpty()) {
            this.customerDao.save(CustomerEntity.builder().identificationId(RANDOM_IDENTIFICATION_ID)
                    .registrationDate(LocalDateTime.now()).lastVisitDate(LocalDateTime.now()).phone(RANDOM_PHONE)
                    .mobilePhone(RANDOM_MOBILE_PHONE).address(RANDOM_ADDRESS).email(RANDOM_EMAIL)
                    .name(RANDOM_NAME).surName(RANDOM_SUR_NAME).secondSurName(RANDOM_SECOND_SUR_NAME).build());
            LogManager.getLogger(this.getClass()).warn("------- Create Customer Random -----------");
        }
    }

}
