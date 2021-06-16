package es.upm.miw.tfm.automundo.configuration;

import es.upm.miw.tfm.automundo.domain.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import reactor.core.publisher.Mono;

@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
public class SecurityConfiguration {

    private final JwtService jwtService;

    @Autowired
    public SecurityConfiguration(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .formLogin().disable()
                .logout().disable()
                .httpBasic()
                .and().addFilterAt(this.JwtAuthenticationWebFilter(), SecurityWebFiltersOrder.FIRST)
                .build();
    }

    private AuthenticationWebFilter JwtAuthenticationWebFilter() {
        AuthenticationWebFilter bearerAuthenticationFilter =
                new AuthenticationWebFilter(new JwtAuthenticationManager(jwtService));
        bearerAuthenticationFilter.setServerAuthenticationConverter(serverWebExchange -> {
            String token = jwtService.extractToken( // token is not verified
                    serverWebExchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
            return Mono.just(new UsernamePasswordAuthenticationToken(token, token)); // it is not authenticated
        });
        return bearerAuthenticationFilter;
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(
            UserDetailsServiceImpl reactiveUserDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        UserDetailsRepositoryReactiveAuthenticationManager manager =
                new UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService);
        manager.setPasswordEncoder(passwordEncoder);
        return manager;
    }

}
