package com.gurgaczj.jmaker.controller.account;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

@ControllerAdvice(basePackages = "com.gurgaczj.jmaker.controller.account")
public class AccountControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public @ResponseBody Mono<String> handle(Exception exception){
        if(exception.getMessage().contains("'name'"))
            return Mono.just("This username is already taken");

        return Mono.just("This email is already taken");
    }
}
