package io.github.jhipster.sample.config;

import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordNonEncoder implements PasswordEncoder{

    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return true;
    }

}
