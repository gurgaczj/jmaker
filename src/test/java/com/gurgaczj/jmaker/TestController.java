package com.gurgaczj.jmaker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@Profile("dev")
public class TestController {

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/test1", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> test1(Principal principal){
        return Mono.just(principal.getName());
    }

    // TODO: add anonymous controller

    @PreAuthorize("hasAnyRole('USER', 'ANONYMOUS')")
    @GetMapping(value = "/test2", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> test2(Principal principal){
        return Mono.just(principal.getName());
    }

}
