package com.gurgaczj.jmaker.validator.register;

import com.gurgaczj.jmaker.model.Register;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractValidator implements Validator<Register, Register> {

    @Override
    public abstract Mono<Register> validate(Register register);

    protected Mono<Register> validatePassword(Register register) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=_-])(?=\\S+$).{8,16}$");
        Matcher matcher = pattern.matcher(register.getPassword());
        return matcher.matches() ? Mono.just(register) : Mono.empty();
    }

    protected Mono<Register> passwordsTheSame(Register register) {
        return register.getPassword().equals(register.getVerifyPassword()) ? Mono.just(register) : Mono.empty();
    }

    protected Mono<Register> validateEmail(Register register) {
        //Regex copyright: https://emailregex.com
        Pattern pattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        Matcher matcher = pattern.matcher(register.getEmail().toLowerCase());
        return matcher.matches() ? Mono.just(register) : Mono.empty();
    }

    protected Mono<Register> validateUsername(Register register) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._-]{6,}$");
        Matcher matcher = pattern.matcher(register.getUsername());
        return matcher.matches() ? Mono.just(register) : Mono.empty();
    }
}
