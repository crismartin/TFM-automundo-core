package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos;

import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.synchronous.UserDao;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.Role;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.UserEntity;
import es.upm.miw.tfm.automundo.infrastructure.enums.StatusRevision;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.synchronous.*;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.*;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import java.util.List;

@Service // @Profile("dev")
public class DatabaseSeederDev {
    private UserDao userDao;
    private CustomerDao customerDao;
    private VehicleDao vehicleDao;
    private ReplacementDao replacementDao;
    private VehicleTypeDao vehicleTypeDao;
    private TechnicianDao technicianDao;
    private RevisionDao revisionDao;
    private ReplacementUsedDao replacementUsedDao;

    private DatabaseStarting databaseStarting;

    @Autowired
    public DatabaseSeederDev(DatabaseStarting databaseStarting, CustomerDao customerDao, VehicleDao vehicleDao,
                             ReplacementDao replacementDao, VehicleTypeDao vehicleTypeDao, TechnicianDao technicianDao,
                             RevisionDao revisionDao, ReplacementUsedDao replacementUsedDao,
                             UserDao userDao) {
        this.databaseStarting = databaseStarting;
        this.userDao = userDao;
        this.customerDao = customerDao;
        this.vehicleDao = vehicleDao;
        this.replacementDao = replacementDao;
        this.vehicleTypeDao = vehicleTypeDao;
        this.technicianDao = technicianDao;
        this.revisionDao = revisionDao;
        this.replacementUsedDao = replacementUsedDao;
        this.deleteAllAndInitializeAndSeedDataBase();
    }

    public void deleteAllAndInitializeAndSeedDataBase() {
        this.deleteAllAndInitialize();
        this.seedDataBaseJava();
    }

    private void deleteAllAndInitialize() {
        this.customerDao.deleteAll();
        this.replacementUsedDao.deleteAll();
        this.revisionDao.deleteAll();
        this.vehicleDao.deleteAll();
        this.replacementDao.deleteAll();
        this.vehicleTypeDao.deleteAll();
        this.technicianDao.deleteAll();
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


        VehicleTypeEntity[] vehicleTypes = {
                VehicleTypeEntity.builder().reference("11111111")
                        .name("Gobierno central").description("Vehículos del gobierno central")
                        .active(true).build(),
                VehicleTypeEntity.builder().reference("22222222")
                        .name("Gobierno autonómico").description("Vehículos del gobierno autonómico")
                        .active(false).build(),
                VehicleTypeEntity.builder().reference("33333333")
                        .name("VTC").description("Vehículos VTC")
                        .active(true).build(),
                VehicleTypeEntity.builder().reference("44444444")
                        .name("Ayuntamiento sanidad").description("Ambulancias, UVI Móviles, SUMA")
                        .active(false).build(),
                VehicleTypeEntity.builder().reference("55555555")
                        .name("Ayuntamiento protección").description("Policía, bomberos ...")
                        .active(true).build(),

        };
        this.vehicleTypeDao.saveAll(List.of(vehicleTypes));
        LogManager.getLogger(this.getClass()).warn("        ------- vehicle types");

        VehicleEntity[] vehicles = {
                VehicleEntity.builder().customer(customers[0]).registerDate(LocalDateTime.now()).lastViewDate(LocalDateTime.now())
                        .model("Tesla Model S").yearRelease(2020).plate("EM-2020").bin("vh-100").id("1lh67i9fds68h3d7809l982376mn")
                        .reference("ref-1001")
                        .vehicleType(vehicleTypes[0]).typeNumber("GOB-123456")
                        .build(),
                VehicleEntity.builder().customer(customers[0]).registerDate(LocalDateTime.now()).lastViewDate(LocalDateTime.now())
                        .model("Ford Fiesta 2020-E").yearRelease(2020).plate("EM-2020").bin("vh-200").id("1lh67i68h3d78dssd09l982376mn")
                        .reference("ref-2002")
                        .vehicleType((vehicleTypes[2]))
                        .build(),
                VehicleEntity.builder().customer(customers[1]).registerDate(LocalDateTime.now()).lastViewDate(LocalDateTime.now())
                        .model("Cupra T").yearRelease(2020).plate("PL-201").bin("cp-100").id("1lh67i9fds68h3d7809l982376mo")
                        .reference("ref-1003")
                        .vehicleType(vehicleTypes[0]).typeNumber("GOB-123456")
                        .build(),
                VehicleEntity.builder().customer(customers[1]).registerDate(LocalDateTime.now()).lastViewDate(LocalDateTime.now())
                        .model("BMW Serie C").yearRelease(2020).plate("EM-2020").bin("cp-200").id("1lh67i9fds68h3d7809l982376mp")
                        .reference("ref-2004")
                        .vehicleType((vehicleTypes[2]))
                        .build(),
                VehicleEntity.builder().customer(customers[1]).registerDate(LocalDateTime.now()).lastViewDate(LocalDateTime.now())
                        .model("Mercedes Clase E").yearRelease(2009).plate("EM-2009").bin("cp-209").id("1lh67i9fds68h3d7809l982376ma")
                        .reference("ref-2055")
                        .vehicleType((vehicleTypes[2]))
                        .build()
        };
        this.vehicleDao.saveAll(List.of(vehicles));
        LogManager.getLogger(this.getClass()).warn("        ------- vehicles");

        ReplacementEntity[] replacements = {
                ReplacementEntity.builder().reference("11111111").name("Pastillas de freno")
                        .price(BigDecimal.valueOf(75.5)).description("Pastillas de freno traseras para coche HONDA Civic")
                        .active(true).build(),
                ReplacementEntity.builder().reference("22222222").name("Pastillas de freno")
                        .price(BigDecimal.valueOf(55.5)).description("Pastillas de freno delanteras para coche HONDA Civic")
                        .active(false).build(),
                ReplacementEntity.builder().reference("33333333").name("Discos de freno")
                        .price(BigDecimal.valueOf(30)).description("Pastillas de freno delanteras para coche RENAULT Clio")
                        .active(true).build(),
                ReplacementEntity.builder().reference("44444444").name("Alternador")
                        .price(BigDecimal.valueOf(49.99)).description("Alternador para motocicleta BMW Z45X")
                        .active(false).build(),
                ReplacementEntity.builder().reference("55555555").name("Fusibles")
                        .price(BigDecimal.valueOf(75.5)).description("Fusibles para motocicleta KAWASAKI RT285")
                        .active(true).build(),
                ReplacementEntity.builder().reference("66666666").name("Motor de arranque")
                        .price(BigDecimal.valueOf(130)).description("Fusibles para coche SEAT León")
                        .active(false).build(),

        };
        this.replacementDao.saveAll(List.of(replacements));
        LogManager.getLogger(this.getClass()).warn("        ------- replacements");

        TechnicianEntity[] technicians = {
                TechnicianEntity.builder().identificationId("11111111-T").ssNumber("SS-1111111")
                        .registrationDate(LocalDateTime.now()).mobile("617271655")
                        .name("Ramón").surName("López").secondSurName("Blanco").active(true).build(),
                TechnicianEntity.builder().identificationId("22222222-T").ssNumber("SS-2222222")
                        .registrationDate(LocalDateTime.now()).leaveDate(LocalDateTime.now()).mobile("643271655")
                        .name("Alfredo").surName("Pérez").secondSurName("Díaz").active(false).build(),
                TechnicianEntity.builder().identificationId("33333333-T").ssNumber("SS-3333333")
                        .registrationDate(LocalDateTime.now()).mobile("655571655")
                        .name("Laura").surName("Molinero").secondSurName("Ramos").active(true).build(),
                TechnicianEntity.builder().identificationId("44444444-T").ssNumber("SS-4444444")
                        .registrationDate(LocalDateTime.now()).leaveDate(LocalDateTime.now()).mobile("688081655")
                        .name("Roberto").surName("López").secondSurName("Fernández").active(false).build(),
        };
        this.technicianDao.saveAll(List.of(technicians));
        LogManager.getLogger(this.getClass()).warn("        ------- technicians");

        RevisionEntity[] revisions = {
                RevisionEntity.builder()
                        .id("revision-id-1")
                        .diagnostic("Cambio en filtro de aceite").registerDate(LocalDateTime.now())
                        .workDescription("Descripcion de la revision 1")
                        .technicianEntity(technicians[0]).status(StatusRevision.POR_CONFIRMAR).cost(new BigDecimal("94.95"))
                        .vehicleEntity(vehicles[0])
                        .leaveDate(null)
                        .reference("rev-1").build(),
                RevisionEntity.builder()
                        .id("revision-id-2")
                        .diagnostic("Cambio en filtro de polen").registerDate(LocalDateTime.now())
                        .workDescription("Descripcion de la revision 2")
                        .technicianEntity(technicians[0]).status(StatusRevision.EN_MANTENIMIENTO).cost(new BigDecimal("0.00"))
                        .vehicleEntity(vehicles[0])
                        .leaveDate(LocalDateTime.now())
                        .reference("rev-2").build(),
                RevisionEntity.builder()
                        .id("revision-id-3")
                        .diagnostic("Cambio en filtro de agua").registerDate(LocalDateTime.now())
                        .workDescription("Descripcion de la revision 3")
                        .technicianEntity(technicians[1]).status(StatusRevision.NEGADO).departureDate(LocalDateTime.now()).cost(new BigDecimal("0.00"))
                        .vehicleEntity(vehicles[0])
                        .leaveDate(null)
                        .reference("rev-3").build(),
                RevisionEntity.builder()
                        .id("revision-id-4")
                        .diagnostic("Cambio en filtro de otra cosa").registerDate(LocalDateTime.now())
                        .workDescription("Descripcion de la revision 4")
                        .technicianEntity(technicians[1]).status(StatusRevision.FINALIZADO).departureDate(LocalDateTime.now()).cost(new BigDecimal("20.00"))
                        .vehicleEntity(vehicles[0])
                        .leaveDate(null)
                        .reference("rev-4").build()
        };
        this.revisionDao.saveAll(List.of(revisions));
        LogManager.getLogger(this.getClass()).warn("        ------- revisions");

        ReplacementUsedEntity[] replacementsUsed = {
                ReplacementUsedEntity.builder()
                        .id("replacementUsed-id-1")
                        .reference("replacementUsed-ref-1")
                        .quantity(1)
                        .discount(10)
                        .price(BigDecimal.valueOf(67.95))
                        .own(true)
                        .replacementEntity(replacements[0])
                        .revisionEntity(revisions[0])
                        .build(),
                ReplacementUsedEntity.builder().id("replacementUsed-id-2")
                        .reference("replacementUsed-ref-2")
                        .quantity(2)
                        .discount(10)
                        .price(BigDecimal.valueOf(27))
                        .own(true)
                        .replacementEntity(replacements[2])
                        .revisionEntity(revisions[0])
                        .build(),
                ReplacementUsedEntity.builder()
                        .id("replacementUsed-id-3")
                        .reference("replacementUsed-ref-3")
                        .quantity(1)
                        .discount(10)
                        .price(BigDecimal.valueOf(10))
                        .own(true)
                        .replacementEntity(replacements[0])
                        .revisionEntity(revisions[3])
                        .build(),
                ReplacementUsedEntity.builder().id("replacementUsed-id-4")
                        .reference("replacementUsed-ref-4")
                        .quantity(2)
                        .discount(10)
                        .price(BigDecimal.valueOf(10))
                        .own(true)
                        .replacementEntity(replacements[2])
                        .revisionEntity(revisions[3])
                        .build()
        };

        this.replacementUsedDao.saveAll(List.of(replacementsUsed));
        LogManager.getLogger(this.getClass()).warn("        ------- replacements used");
    }

}



