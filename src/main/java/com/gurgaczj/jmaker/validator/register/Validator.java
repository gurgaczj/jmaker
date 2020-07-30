package com.gurgaczj.jmaker.validator.register;

import com.gurgaczj.jmaker.model.Register;
import reactor.core.publisher.Mono;

public interface Validator<S, R> {

    Mono<R> validate(S register);
}
