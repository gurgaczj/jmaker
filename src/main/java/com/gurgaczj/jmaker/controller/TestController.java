package com.gurgaczj.jmaker.controller;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
public class TestController {

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/dupa", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<String> asdf(Principal principal){
        return Mono.just("sadadsafsf ");
    }

}
