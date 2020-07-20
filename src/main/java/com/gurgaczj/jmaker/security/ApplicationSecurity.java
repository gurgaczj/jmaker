package com.gurgaczj.jmaker.security;

import com.gurgaczj.jmaker.security.auth.AuthenticationResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class ApplicationSecurity {

    private final AuthenticationResponse authenticationResponse;

    public ApplicationSecurity(AuthenticationResponse authenticationResponse) {
        this.authenticationResponse = authenticationResponse;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        http
                .csrf().disable()
                .authorizeExchange()
                .anyExchange().authenticated()
                .and()
                .httpBasic().disable()
                .formLogin()
                    .authenticationSuccessHandler(authenticationResponse::onAuthenticationSuccess)
                    .authenticationFailureHandler(authenticationResponse::onAuthenticationFailure);
        return http.build();
    }

}
