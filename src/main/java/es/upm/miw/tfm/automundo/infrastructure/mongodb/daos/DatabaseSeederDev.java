package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos;

import es.upm.miw.tfm.automundo.domain.model.VehicleType;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.synchronous.*;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.*;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service // @Profile("dev")
public class DatabaseSeederDev {
    private CustomerDao customerDao;
    private VehicleDao vehicleDao;
    private OwnerTypeDao ownerTypeDao;
    private ReplacementDao replacementDao;
    private VehicleTypeDao vehicleTypeDao;

    private DatabaseStarting databaseStarting;

    @Autowired
    public DatabaseSeederDev(DatabaseStarting databaseStarting, CustomerDao customerDao, VehicleDao vehicleDao,
                             OwnerTypeDao ownerTypeDao, ReplacementDao replacementDao, VehicleTypeDao vehicleTypeDao) {
        this.databaseStarting = databaseStarting;
        this.customerDao = customerDao;
        this.vehicleDao = vehicleDao;
        this.ownerTypeDao = ownerTypeDao;
        this.replacementDao = replacementDao;
        this.vehicleTypeDao = vehicleTypeDao;
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
        this.replacementDao.deleteAll();
        this.vehicleTypeDao.deleteAll();
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

        ReplacementEntity[] replacements = {
                ReplacementEntity.builder().reference("11111111").name("Pastillas de freno")
                        .price(new BigDecimal(75.5)).description("Pastillas de freno traseras para coche HONDA Civic")
                        .build(),
                ReplacementEntity.builder().reference("22222222").name("Pastillas de freno")
                        .price(new BigDecimal(55.5)).description("Pastillas de freno delanteras para coche HONDA Civic")
                        .build(),
                ReplacementEntity.builder().reference("33333333").name("Discos de freno")
                        .price(new BigDecimal(30)).description("Pastillas de freno delanteras para coche RENAULT Clio")
                        .build(),
                ReplacementEntity.builder().reference("44444444").name("Alternador")
                        .price(new BigDecimal(49.99)).description("Alternador para motocicleta BMW Z45X")
                        .build(),
                ReplacementEntity.builder().reference("55555555").name("Fusibles")
                        .price(new BigDecimal(75.5)).description("Fusibles para motocicleta KAWASAKI RT285")
                        .build(),
                ReplacementEntity.builder().reference("66666666").name("Motor de arranque")
                        .price(new BigDecimal(130)).description("Fusibles para coche SEAT León")
                        .build(),

        };
        this.replacementDao.saveAll(List.of(replacements));
        LogManager.getLogger(this.getClass()).warn("        ------- replacements");

        VehicleTypeEntity[] vehicleTypes = {
                VehicleTypeEntity.builder().reference("11111111")
                        .name("Gobierno central").description("Vehículos del gobierno central").build(),
                VehicleTypeEntity.builder().reference("22222222")
                        .name("Gobierno autonómico").description("Vehículos del gobierno autonómico").build(),
                VehicleTypeEntity.builder().reference("33333333")
                        .name("VTC").description("Vehículos VTC").build(),
                VehicleTypeEntity.builder().reference("44444444")
                        .name("Ayuntamiento sanidad").description("Ambulancias, UVI Móviles, SUMA").build(),
                VehicleTypeEntity.builder().reference("44444444")
                        .name("Ayuntamiento protección").description("Policía, bomberos ...").build(),

        };
        this.vehicleTypeDao.saveAll(List.of(vehicleTypes));
        LogManager.getLogger(this.getClass()).warn("        ------- vehicle-types");

    }

}



