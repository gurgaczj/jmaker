package com.gurgaczj.jmaker.security.auth;

import com.gurgaczj.jmaker.jwt.JwtUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationResponse implements ServerAuthenticationSuccessHandler, ServerAuthenticationFailureHandler {

    private final JwtUtils jwtUtils;
    private final static String HEADER = "Bearer";

    public AuthenticationResponse(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        return Mono.just(webFilterExchange.getExchange().getResponse())
                .flatMap(serverHttpResponse -> addTokenToHeader(serverHttpResponse, authentication));
    }

    private Mono<? extends Void> addTokenToHeader(ServerHttpResponse serverHttpResponse, Authentication authentication) {
        String jwt = jwtUtils.generateToken(authentication.getName(), authentication.getAuthorities());
        serverHttpResponse.getHeaders().add(HttpHeaders.AUTHORIZATION, String.join(" ", HEADER, jwt));
        serverHttpResponse.setStatusCode(HttpStatus.OK);
        return serverHttpResponse.setComplete();
    }

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        return Mono.just(webFilterExchange.getExchange().getResponse())
                .flatMap(serverHttpResponse -> createFailureResponse(serverHttpResponse, exception));
    }

    private Mono<? extends Void> createFailureResponse(ServerHttpResponse serverHttpResponse, AuthenticationException exception) {
        serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
        return serverHttpResponse.writeWith(
                Mono.just(serverHttpResponse.bufferFactory().wrap(exception.getMessage().getBytes()))
        );
    }
}
