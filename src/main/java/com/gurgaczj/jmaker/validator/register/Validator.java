package com.gurgaczj.jmaker.validator.register;

import reactor.core.publisher.Mono;

public interface Validator<S, R> {

    Mono<R> validate(S register);
}
