package com.gurgaczj.jmaker.security;

import com.gurgaczj.jmaker.security.auth.AuthenticationResponse;
import com.gurgaczj.jmaker.security.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class ApplicationSecurity {

    private final AuthenticationResponse authenticationResponse;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public ApplicationSecurity(AuthenticationResponse authenticationResponse,
                               @Qualifier("jwtAuthenticationFilter") JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationResponse = authenticationResponse;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        http
                //.exceptionHandling(new CustomExceptionHandler())
                .anonymous().authorities("ROLE_ANONYMOUS")
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .formLogin()
                    .authenticationSuccessHandler(authenticationResponse::onAuthenticationSuccess)
                    .authenticationFailureHandler(authenticationResponse::onAuthenticationFailure);

       http.addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION);
//       http.authorizeExchange().pathMatchers(HttpMethod.GET, "/dupa").hasRole("USER");
        return http.build();
    }

}
