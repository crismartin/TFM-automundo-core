package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos;

import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.synchronous.CustomerDao;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.CustomerEntity;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service // @Profile("dev")
public class DatabaseSeederDev {
    private CustomerDao customerDao;

    private DatabaseStarting databaseStarting;

    @Autowired
    public DatabaseSeederDev(DatabaseStarting databaseStarting, CustomerDao customerDao) {
        this.databaseStarting = databaseStarting;
        this.customerDao = customerDao;
        this.deleteAllAndInitializeAndSeedDataBase();
    }

    public void deleteAllAndInitializeAndSeedDataBase() {
        this.deleteAllAndInitialize();
        this.seedDataBaseJava();
    }

    private void deleteAllAndInitialize() {
        this.customerDao.deleteAll();
        LogManager.getLogger(this.getClass()).warn("------- Delete All -------");
        this.databaseStarting.initialize();
    }

    private void seedDataBaseJava() {
        LogManager.getLogger(this.getClass()).warn("------- Initial Load from JAVA --------");
        CustomerEntity[] customers = {
                CustomerEntity.builder().identificationId("22222222-A")
                        .registrationDate(LocalDateTime.now()).lastVisitDate(LocalDateTime.now()).phone("222222222")
                        .mobilePhone("444444444").address("C/ Chile 123, Madrid").email("cliente2@gmail.com")
                        .name("Laura").surName("García").secondSurName("Contreras").build(),
                CustomerEntity.builder().identificationId("33333333-A")
                        .registrationDate(LocalDateTime.now()).lastVisitDate(LocalDateTime.now()).phone("333333333")
                        .mobilePhone("555555555").address("C/ Suiza 123, Madrid").email("cliente3@gmail.com")
                        .name("Lucía").surName("García").secondSurName("Perales").build(),
        };
        this.customerDao.saveAll(List.of(customers));
        LogManager.getLogger(this.getClass()).warn("        ------- customers");
    }

}



