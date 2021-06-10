package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos;

import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.synchronous.CustomerDao;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.synchronous.OwnerTypeDao;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.synchronous.VehicleDao;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.CustomerEntity;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.OwnerTypeEntity;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.VehicleEntity;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service // @Profile("dev")
public class DatabaseSeederDev {
    private CustomerDao customerDao;
    private VehicleDao vehicleDao;
    private OwnerTypeDao ownerTypeDao;

    private DatabaseStarting databaseStarting;

    @Autowired
    public DatabaseSeederDev(DatabaseStarting databaseStarting, CustomerDao customerDao, VehicleDao vehicleDao, OwnerTypeDao ownerTypeDao) {
        this.databaseStarting = databaseStarting;
        this.customerDao = customerDao;
        this.vehicleDao = vehicleDao;
        this.ownerTypeDao = ownerTypeDao;
        this.deleteAllAndInitializeAndSeedDataBase();
    }

    public void deleteAllAndInitializeAndSeedDataBase() {
        this.deleteAllAndInitialize();
        this.seedDataBaseJava();
    }

    private void deleteAllAndInitialize() {
        this.customerDao.deleteAll();
        this.vehicleDao.deleteAll();
        this.ownerTypeDao.deleteAll();
        LogManager.getLogger(this.getClass()).warn("------- Delete All -------");
        this.databaseStarting.initialize();
    }

    private void seedDataBaseJava() {
        LogManager.getLogger(this.getClass()).warn("------- Initial Load from JAVA --------");
        CustomerEntity[] customers = {
                CustomerEntity.builder().identificationId("22222222-A").id("id_customer_2")
                        .registrationDate(LocalDateTime.now()).lastVisitDate(LocalDateTime.now()).phone("222222222")
                        .mobilePhone("444444444").address("C/ Chile 123, Madrid").email("cliente2@gmail.com")
                        .name("Laura").surName("García").secondSurName("Contreras").build(),
                CustomerEntity.builder().identificationId("33333333-A").id("id_customer_3")
                        .registrationDate(LocalDateTime.now()).lastVisitDate(LocalDateTime.now()).phone("333333333")
                        .mobilePhone("555555555").address("C/ Suiza 123, Madrid").email("cliente3@gmail.com")
                        .name("Lucía").surName("García").secondSurName("Perales").build(),
        };
        this.customerDao.saveAll(List.of(customers));
        LogManager.getLogger(this.getClass()).warn("        ------- customers");

        OwnerTypeEntity[] ownerTypes = {
                OwnerTypeEntity.builder().id("4lh67i968h3d7809l982376mn").name("Particular").reference("1").build(),
                OwnerTypeEntity.builder().id("3lh67i968h3d7809l982376mn").name("Gobierno").reference("2").build()
        };
        this.ownerTypeDao.saveAll(List.of(ownerTypes));
        LogManager.getLogger(this.getClass()).warn("        ------- ownerTypes");

        VehicleEntity[] vehicles = {
                VehicleEntity.builder().customer(customers[0]).registerDate(LocalDateTime.now()).lastViewDate(LocalDateTime.now())
                        .model("Tesla Model S").yearRelease(2020).plate("EM-2020").bin("vh-1001").id("1lh67i9fds68h3d7809l982376mn")
                        .reference("ref-1001")
                        .ownerType(ownerTypes[1]).ownerNumber("GOB-123456")
                        .build(),
                VehicleEntity.builder().customer(customers[0]).registerDate(LocalDateTime.now()).lastViewDate(LocalDateTime.now())
                        .model("Ford Fiesta 2020-E").yearRelease(2020).plate("EM-2020").bin("vh-2002").id("1lh67i68h3d78dssd09l982376mn")
                        .reference("ref-2002")
                        .ownerType(ownerTypes[0])
                        .build()
        };
        this.vehicleDao.saveAll(List.of(vehicles));
        LogManager.getLogger(this.getClass()).warn("        ------- vehicles");

    }

}



