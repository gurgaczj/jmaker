package com.gurgaczj.jmaker.validator.register;

import com.gurgaczj.jmaker.exception.RegisterException;
import com.gurgaczj.jmaker.model.Register;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RegisterValidatorImpl extends AbstractValidator {

    @Override
    public Mono<Register> validate(Register register) {
        return Mono.just(register)
                .flatMap(r -> validatePassword(r))
                .switchIfEmpty(Mono.error(new RegisterException("Password does not meet requirements")))
                .flatMap(r -> passwordsTheSame(r))
                .switchIfEmpty(Mono.error(new RegisterException("Passwords are not the same")))
                .flatMap(r -> validateEmail(r))
                .switchIfEmpty(Mono.error(new RegisterException("Email does not meet requirements")))
                .flatMap(r -> validateUsername(r))
                .switchIfEmpty(Mono.error(new RegisterException("Username does not meet requirements")));
        //.doOnError(RegisterException.class, Mono::error);
    }
}
