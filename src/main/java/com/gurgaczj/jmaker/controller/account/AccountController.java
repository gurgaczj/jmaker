package com.gurgaczj.jmaker.controller.account;

import com.gurgaczj.jmaker.dto.AccountDto;
import com.gurgaczj.jmaker.dto.AccountLessInfoDto;
import com.gurgaczj.jmaker.model.NewPassword;
import com.gurgaczj.jmaker.model.Register;
import com.gurgaczj.jmaker.service.AccountService;
import com.gurgaczj.jmaker.service.RegisterService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final RegisterService registerService;
    private final AccountService accountService;

    public AccountController(RegisterService registerService, AccountService accountService) {
        this.registerService = registerService;
        this.accountService = accountService;
    }

    @Operation(description = "Register account")
    @PreAuthorize("hasRole('ANONYMOUS')")
    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<AccountDto> registerAccount(@RequestBody Register registerModel){
        return registerService.register(registerModel);
    }

    @Operation(description = "Verifies account")
    @PreAuthorize("hasRole('ANONYMOUS')")
    @PostMapping(path = "/verify", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<AccountDto> verifyAccount(@RequestParam("account") String verificationCode){
        return registerService.verifyAccount(verificationCode);
    }

    @Operation(description = "Return account data of authorized user via JSON Web Token")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'TUTOR', 'SENIOR_TUTOR', 'GAME_MASTER', 'COMMUNITY_MANAGER', 'ADMIN')")
    public Mono<AccountDto> getAccount(Principal principal){
        return accountService.getAccount(principal);
    }

    @Operation(description = "Return account data by account name. Available for anyone")
    @GetMapping(value = "/{accountName}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'TUTOR', 'SENIOR_TUTOR', 'GAME_MASTER', 'COMMUNITY_MANAGER', 'ADMIN', 'ANONYMOUS')")
    public Mono<AccountLessInfoDto> getAccountByName(@PathVariable String accountName){
        return accountService.getAccountByName(accountName);
    }

    //TODO: move to admin controller
    @Operation(description = "Edit account data. Admin only")
    @PutMapping(value = "/{accountName}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<AccountDto> editAccount(@PathVariable String accountName, @RequestBody AccountDto accountData){
        return accountService.editAccount(accountName, accountData);
    }
}
