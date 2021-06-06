package es.upm.miw.tfm.automundo.domain.services;

import es.upm.miw.tfm.automundo.configuration.JwtService;
import es.upm.miw.tfm.automundo.domain.persistence.UserPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class UserService {

    private UserPersistence userPersistence;
    private JwtService jwtService;

    @Autowired
    public UserService(UserPersistence userPersistence, JwtService jwtService) {
        this.userPersistence = userPersistence;
        this.jwtService = jwtService;
    }

    public Mono<String> login(String userName) {
        return this.userPersistence.readByUserName(userName)
                .map(user -> jwtService.createToken(user.getUserName(), user.getRealName(), user.getRole().name()));
    }
}
