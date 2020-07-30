package com.gurgaczj.jmaker.exception;

import com.gurgaczj.jmaker.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class AccessDeniedHandler {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public @ResponseBody Mono<ErrorMessage> handle(){
        return Mono.just(ErrorMessage.create(HttpStatus.FORBIDDEN, "Access Denied"));
    }
}
