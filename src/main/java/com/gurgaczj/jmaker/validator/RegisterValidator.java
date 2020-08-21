package com.gurgaczj.jmaker.validator;

import com.gurgaczj.jmaker.model.Register;
import reactor.core.publisher.Mono;

public interface RegisterValidator {

    Mono<Register> validateRegisterModel(Register register);

    boolean validatePassword(String password);

    boolean passwordsTheSame(String password, String verifyPassword);

    boolean validateEmail(String emial);

    boolean validateUsername(String username);
}
