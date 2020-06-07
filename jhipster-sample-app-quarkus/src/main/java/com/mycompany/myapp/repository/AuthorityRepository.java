package com.mycompany.myapp.repository;

import javax.enterprise.context.ApplicationScoped;

import com.mycompany.myapp.domain.Authority;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

@ApplicationScoped
public class AuthorityRepository implements PanacheRepositoryBase<Authority, String> {
}
