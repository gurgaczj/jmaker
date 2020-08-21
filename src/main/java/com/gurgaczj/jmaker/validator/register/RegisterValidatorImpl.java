package com.gurgaczj.jmaker.validator.register;

import com.gurgaczj.jmaker.exception.RegisterException;
import com.gurgaczj.jmaker.model.Register;
import com.gurgaczj.jmaker.validator.EmailValidator;
import com.gurgaczj.jmaker.validator.PasswordValidator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RegisterValidatorImpl extends AbstractRegisterValidator implements PasswordValidator, EmailValidator {

    @Override
    public Mono<Register> validate(Register register) {
        return Mono.just(register)
                .flatMap(re -> {
                    if(!super.validatePassword(re.getPassword()))
                        return Mono.error(new RegisterException("Password does not meet requirements"));

                    if(!super.passwordsTheSame(re.getPassword(), re.getVerifyPassword()))
                        return Mono.error(new RegisterException("Passwords are not the same"));

                    if(!super.validateEmail(re.getEmail()))
                        return Mono.error(new RegisterException("Email does not meet requirements"));

                    if(!super.validateUsername(re.getUsername()))
                        return Mono.error(new RegisterException("Username does not meet requirements"));

                    return Mono.just(re);
                });

    }

    @Override
    public boolean validatePassword(String password) {
        return super.validatePassword(password);
    }

    @Override
    public boolean passwordsTheSame(String password, String verifyPassword) {
        return super.passwordsTheSame(password, verifyPassword);
    }

    @Override
    public boolean validateEmail(String emial) {
        return super.validateEmail(emial);
    }
}
