package com.gurgaczj.jmaker.validator;

import reactor.core.publisher.Mono;

public interface RegisterValidator<S, R> {

    Mono<R> validate(S register);
}
