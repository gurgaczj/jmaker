package com.gurgaczj.jmaker.validator.register;

import com.gurgaczj.jmaker.exception.RegisterException;
import com.gurgaczj.jmaker.model.Register;
import com.gurgaczj.jmaker.validator.RegisterValidator;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterRegisterValidatorTest {

    RegisterValidator<Register, Register> registerValidator = new RegisterValidatorImpl();

    @Test
    public void registerValidatorTest(){
        Register register = getRegisterModel();

        StepVerifier
                .create(registerValidator.validate(register))
                .assertNext(reg -> {
                    assertEquals(reg, register);
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void registerValidatorTest_wrongUsername(){
        Register register = getRegisterModel();
        register.setUsername("das");

        StepVerifier
                .create(registerValidator.validate(register))
                .expectError(RegisterException.class)
//                .expectComplete()
                .verify();
    }

    @Test
    public void registerValidatorTest_wrongEmail(){
        Register register = getRegisterModel();
        register.setEmail("mail.gfer-s@x");

        StepVerifier
                .create(registerValidator.validate(register))
                .expectError(RegisterException.class)
                .verify();
    }

    @Test
    public void registerValidatorTest_passwordTooShort(){
        Register register = getRegisterModel();
        register.setPassword("Pass1@");

        StepVerifier
                .create(registerValidator.validate(register))
                .expectError(RegisterException.class)
                .verify();
    }

    @Test
    public void registerValidatorTest_passwordWithoutNumber(){
        Register register = getRegisterModel();
        register.setPassword("Password@");

        StepVerifier
                .create(registerValidator.validate(register))
                .expectError(RegisterException.class)
                .verify();
    }

    @Test
    public void registerValidatorTest_passwordWithoutUppercaseChar(){
        Register register = getRegisterModel();
        register.setPassword("password@1");

        StepVerifier
                .create(registerValidator.validate(register))
                .expectError(RegisterException.class)
                .verify();
    }

    @Test
    public void registerValidatorTest_passwordWithoutSpecialChar(){
        Register register = getRegisterModel();
        register.setPassword("password1");

        StepVerifier
                .create(registerValidator.validate(register))
                .expectError(RegisterException.class)
                .verify();
    }

    @Test
    public void registerValidatorTest_passwordsNotTheSame(){
        Register register = getRegisterModel();
        register.setVerifyPassword("password1!");

        StepVerifier
                .create(registerValidator.validate(register))
                .expectError(RegisterException.class)
                .verify();
    }

    public static Register getRegisterModel(){
        Register register = new Register();
        register.setUsername("testuser");
        register.setPassword("Password1!");
        register.setVerifyPassword("Password1!");
        register.setEmail("testuser@mail.com");

        return register;
    }
}
