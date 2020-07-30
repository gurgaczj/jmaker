package com.gurgaczj.jmaker.security;

import com.gurgaczj.jmaker.security.encoder.Sha1PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Sha1PasswordEncoderTests {

    @Test
    public void encodeTest() {
        PasswordEncoder passwordEncoder = new Sha1PasswordEncoder();

        String password = "pass";

        String encodedPass = passwordEncoder.encode(password);

        assertNotNull(encodedPass);

        assertTrue(passwordEncoder.matches(password, encodedPass));
    }
}
