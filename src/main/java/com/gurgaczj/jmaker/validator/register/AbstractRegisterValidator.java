package com.gurgaczj.jmaker.validator.register;

import com.gurgaczj.jmaker.model.Register;
import com.gurgaczj.jmaker.validator.RegisterValidator;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractRegisterValidator implements RegisterValidator<Register, Register> {

    @Override
    public abstract Mono<Register> validate(Register register);

    protected boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=_-])(?=\\S+$).{8,16}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    protected boolean passwordsTheSame(String password, String verifyPassword) {
        return password.equals(verifyPassword);
    }

    protected boolean validateEmail(String email) {
        //Regex copyright: https://emailregex.com
        Pattern pattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        Matcher matcher = pattern.matcher(email.toLowerCase());
        return matcher.matches();
    }

    protected boolean validateUsername(String username) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._-]{6,}$");
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
}
