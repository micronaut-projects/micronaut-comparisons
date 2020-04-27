package io.github.jhipster.sample.security;

import javax.inject.Singleton;

import io.micronaut.context.annotation.Replaces;

@Singleton
@Replaces(BcryptPasswordEncoder.class)
public class PasswordNonEncoder implements PasswordEncoder {

    @Override
    public String encode(String rawPassword) {
        return rawPassword;
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return true;
    }
}
