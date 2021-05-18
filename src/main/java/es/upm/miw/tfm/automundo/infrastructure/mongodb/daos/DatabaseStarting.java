package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseStarting {

    //private RepoDao repoDao;

    @Autowired
    public DatabaseStarting() {
        // this.repoDao = repoDao;
        this.initialize();
    }

    void initialize() {

    }

}
