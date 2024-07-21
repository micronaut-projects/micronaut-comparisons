package com.mycompany.myapp.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.mycompany.myapp.domain.BankAccount;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

/**
 * Spring Data  repository for the BankAccount entity.
 */
@ApplicationScoped
public class BankAccountRepository implements PanacheRepositoryBase<BankAccount, Long> {

    List<BankAccount> findByUserIsCurrentUser() {
        return list("bankAccount.user.login = ?#{principal.username}");
    }
}
