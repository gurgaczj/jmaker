package com.gurgaczj.jmaker.security.filter;

import com.gurgaczj.jmaker.security.auth.JwtAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter extends AuthenticationWebFilter {

    public JwtAuthenticationFilter(ServerAuthenticationConverter jwtConverter) {
        super(new JwtAuthenticationManager());
        super.setServerAuthenticationConverter(jwtConverter);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return super.filter(exchange, chain);
    }

    @Override
    protected Mono<Void> onAuthenticationSuccess(Authentication authentication, WebFilterExchange webFilterExchange) {
        //TODO: update last login date
        return super.onAuthenticationSuccess(authentication, webFilterExchange);
    }
}
