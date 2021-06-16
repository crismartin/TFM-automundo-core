package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos;

import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.synchronous.UserDao;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.Role;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.UserEntity;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service // @Profile("dev")
public class DatabaseSeederDev {
    private UserDao userDao;

    private DatabaseStarting databaseStarting;

    @Autowired
    public DatabaseSeederDev(DatabaseStarting databaseStarting, UserDao userDao) {
        this.databaseStarting = databaseStarting;
        this.userDao = userDao;
        this.deleteAllAndInitializeAndSeedDataBase();
    }

    public void deleteAllAndInitializeAndSeedDataBase() {
        this.deleteAllAndInitialize();
        this.seedDataBaseJava();
    }

    private void deleteAllAndInitialize() {
        this.userDao.deleteAll();
        LogManager.getLogger(this.getClass()).warn("------- Delete All -------");
        this.databaseStarting.initialize();
    }

    private void seedDataBaseJava() {
        LogManager.getLogger(this.getClass()).warn("------- Initial Load from JAVA --------");
        String pass = new BCryptPasswordEncoder().encode("9");
        UserEntity[] users = {
                UserEntity.builder().userName("9A").realName("admin2")
                        .surName("admin2").secondSurName("admin2").dni("00000000B")
                        .password(new BCryptPasswordEncoder().encode(pass))
                        .role(Role.ADMIN).registrationDate(LocalDateTime.now()).build()
        };
        this.userDao.saveAll(Arrays.asList(users));
        LogManager.getLogger(this.getClass()).warn("        ------- users");
    }

}



