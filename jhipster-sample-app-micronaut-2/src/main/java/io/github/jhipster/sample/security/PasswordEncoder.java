package io.github.jhipster.sample.security;

public interface PasswordEncoder {
    public String encode(String rawPassword);
    public boolean matches(String rawPassword, String encodedPassword);
}
