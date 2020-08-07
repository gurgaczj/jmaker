package com.gurgaczj.jmaker.controller.account;

import com.gurgaczj.jmaker.dto.AccountDto;
import com.gurgaczj.jmaker.model.Register;
import com.gurgaczj.jmaker.service.RegisterService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final RegisterService registerService;

    public AccountController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @Operation(description = "Register account")
    @PreAuthorize("hasRole('ANONYMOUS')")
    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<AccountDto> registerAccount(@RequestBody Register registerModel){
        return registerService.register(registerModel);
    }

    @Operation(description = "Verifies account")
    @PreAuthorize("hasRole('ANONYMOUS')")
    @GetMapping(path = "/verify", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Mono<AccountDto>> verifyAccount(@PathVariable("hash") String verificationCode){
        return Mono.just(registerService.verifyAccount(verificationCode));
    }
}
