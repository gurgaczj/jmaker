package com.gurgaczj.jmaker.validator.register;

public interface PasswordValidator {

    boolean validatePassword(String password);

    boolean passwordsTheSame(String password, String verifyPassword);
}
