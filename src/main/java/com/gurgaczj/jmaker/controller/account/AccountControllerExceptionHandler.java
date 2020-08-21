package com.gurgaczj.jmaker.controller.account;

import com.gurgaczj.jmaker.exception.*;
import com.gurgaczj.jmaker.model.ErrorMessage;
import io.r2dbc.spi.R2dbcDataIntegrityViolationException;
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
    @ExceptionHandler({DataIntegrityViolationException.class, R2dbcDataIntegrityViolationException.class})
    public @ResponseBody
    Mono<ErrorMessage> handleViolationOfUniqueValue(Exception exception) {
        if (exception.getMessage().contains("'name'"))
            return Mono.just(ErrorMessage.create(HttpStatus.BAD_REQUEST, "This username is already taken"));

        return Mono.just(ErrorMessage.create(HttpStatus.BAD_REQUEST, "This email is already taken"));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({MailSenderException.class, InternalServerException.class})
    public @ResponseBody
    Mono<ErrorMessage> handleMailSendFailure(Exception e) {
        return Mono.just(ErrorMessage.create(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({RegisterException.class, NotFoundException.class, ValidationException.class})
    public @ResponseBody
    Mono<ErrorMessage> handleRegisterError(Exception e) {
        return Mono.just(ErrorMessage.create(HttpStatus.BAD_REQUEST, e.getMessage()));
    }
}
