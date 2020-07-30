package com.gurgaczj.jmaker.service;

import com.gurgaczj.jmaker.dto.AccountDto;
import com.gurgaczj.jmaker.model.Register;
import reactor.core.publisher.Mono;

public interface RegisterService {

    Mono<AccountDto> register(Register register);
}
