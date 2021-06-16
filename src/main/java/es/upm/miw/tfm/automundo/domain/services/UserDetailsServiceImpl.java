package es.upm.miw.tfm.automundo.domain.services;

import es.upm.miw.tfm.automundo.domain.persistence.UserPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.Role;

import java.util.ArrayList;
import java.util.List;

@Service("reactiveUserDetailsService")
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    private UserPersistence userPersistence;

    @Autowired
    public UserDetailsServiceImpl(UserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }

    private Mono<org.springframework.security.core.userdetails.User> userBuilder(String userName, String password, Role[] roles) {
        List< GrantedAuthority > authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.withPrefix()));
        }
        return Mono.just(new org.springframework.security.core.userdetails.User(userName, password, true, true,
                true, true, authorities));
    }

    @Override
    public Mono<UserDetails> findByUsername(String userName) {
        return this.userPersistence.readByUserName(userName)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Non existent user: " + userName)))
                .flatMap(user -> this.userBuilder(user.getUserName(), user.getPassword(), new Role[]{Role.AUTHENTICATED}));
    }
}
