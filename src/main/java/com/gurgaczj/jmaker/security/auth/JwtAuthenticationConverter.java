package com.gurgaczj.jmaker.security.auth;

import com.gurgaczj.jmaker.jwt.JwtUtils;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
public class JwtAuthenticationConverter implements ServerAuthenticationConverter {

    private final static Logger logger = LoggerFactory.getLogger(JwtAuthenticationConverter.class);

    private final static String BEARER = "Bearer ";
    private final Function<String, String> typeRemover = token -> token.replaceFirst(BEARER, "");

    private final JwtUtils jwtUtils;

    public JwtAuthenticationConverter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();

        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (token == null) {
            return Mono.empty();
        }

        token = typeRemover.apply(token);

        if (token.isBlank()) {
            return Mono.empty();
        }

        try {
            return Mono.just(new UsernamePasswordAuthenticationToken(
                    jwtUtils.getIssuer(token), null, jwtUtils.getGrantedAuthorities(token)));
        } catch (JwtException e) {
            logger.info(e.getMessage());
            return Mono.empty();
        }
    }
}
