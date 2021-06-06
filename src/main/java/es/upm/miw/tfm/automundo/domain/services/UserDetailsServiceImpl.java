package es.upm.miw.tfm.automundo.domain.services;

import es.upm.miw.tfm.automundo.domain.persistence.UserPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import es.upm.miw.tfm.automundo.domain.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.Role;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Qualifier("miw.users")
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserPersistence userPersistence;

    @Autowired
    public UserDetailsServiceImpl(UserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }

    @Override
    public UserDetails loadUserByUsername(final String userName) {
        return (UserDetails) this.userPersistence.readByUserName(userName)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Non existent user: " + userName)))
                .map(user -> this.userBuilder(user.getUserName(), user.getPassword(), new Role[]{Role.AUTHENTICATED}));
        /*User user =  this.userPersistence.readByUserName(userName)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Non existent user: " + userName)))
                .block();
        return this.userBuilder(user.getUserName(), user.getPassword(), new Role[]{Role.AUTHENTICATED});*/

    }

    private org.springframework.security.core.userdetails.User userBuilder(String userName, String password, Role[] roles) {
        List< GrantedAuthority > authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.withPrefix()));
        }
        return new org.springframework.security.core.userdetails.User(userName, password, true, true,
                true, true, authorities);
    }
}
