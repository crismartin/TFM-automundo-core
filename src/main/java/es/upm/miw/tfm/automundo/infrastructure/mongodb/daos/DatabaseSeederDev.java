package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // @Profile("dev")
public class DatabaseSeederDev {
    // private RepoDao repoDao;

    private DatabaseStarting databaseStarting;

    @Autowired
    public DatabaseSeederDev() {
        // this.repoDao = repoDao;
        //this.deleteAllAndInitializeAndSeedDataBase();
    }

    public void deleteAllAndInitializeAndSeedDataBase() {
        this.deleteAllAndInitialize();
        this.seedDataBaseJava();
    }

    private void deleteAllAndInitialize() {
        LogManager.getLogger(this.getClass()).warn("------- Delete All -------");
        this.databaseStarting.initialize();
    }

    private void seedDataBaseJava() {
        LogManager.getLogger(this.getClass()).warn("------- Initial Load from JAVA --------");
        /*
        Entity[] entities = {};
        this.repoDao.saveAll(List.of(entities));
        LogManager.getLogger(this.getClass()).warn("        ------- entities");
        */
    }

}



