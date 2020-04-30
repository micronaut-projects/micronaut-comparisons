package com.mycompany.myapp.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mycompany.myapp.domain.BankAccount;
import com.mycompany.myapp.repository.BankAccountRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.util.HeaderUtil;
import com.mycompany.myapp.web.util.ResponseUtil;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.BankAccount}.
 */
@Path("/api/bank-accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class BankAccountResource {

    private final Logger log = LoggerFactory.getLogger(BankAccountResource.class);

    private static final String ENTITY_NAME = "bankAccount";

    private final String applicationName;

    private final BankAccountRepository bankAccountRepository;

    @Inject
    public BankAccountResource(@ConfigProperty(name = "application.name") String applicationName, BankAccountRepository bankAccountRepository) {
        this.applicationName = applicationName;
        this.bankAccountRepository = bankAccountRepository;
    }

    /**
     * {@code POST  /bank-accounts} : Create a new bankAccount.
     *
     * @param bankAccount the bankAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bankAccount, or with status {@code 400 (Bad Request)} if the bankAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @POST
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    public Response createBankAccount(@Valid BankAccount bankAccount) throws URISyntaxException {
        log.debug("REST request to save BankAccount : {}", bankAccount);
        if (bankAccount.getId() != null) {
            throw new BadRequestAlertException("A new bankAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bankAccountRepository.persist(bankAccount);
        Response.ResponseBuilder response = Response.created(new URI("/api/bank-accounts/" + bankAccount.getId()))
            .entity(bankAccount);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, bankAccount.getId().toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /bank-accounts} : Updates an existing bankAccount.
     *
     * @param bankAccount the bankAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bankAccount,
     * or with status {@code 400 (Bad Request)} if the bankAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bankAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PUT
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    public Response updateBankAccount(@Valid BankAccount bankAccount) throws URISyntaxException {
        log.debug("REST request to update BankAccount : {}", bankAccount);
        if (bankAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        bankAccountRepository.persist(bankAccount);

        Response.ResponseBuilder response = Response.ok(bankAccount);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bankAccount.getId().toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /bank-accounts} : get all the bankAccounts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bankAccounts in body.
     */
    @GET
    @RolesAllowed("ROLE_ADMIN")
    public List<BankAccount> getAllBankAccounts() {
        log.debug("REST request to get all BankAccounts");
        return bankAccountRepository.findAll().withHint("org.hibernate.cacheable", Boolean.TRUE).list();
    }

    /**
     * {@code GET  /bank-accounts/:id} : get the "id" bankAccount.
     *
     * @param id the id of the bankAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bankAccount, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public Response getBankAccount(@PathParam("id") Long id) {
        log.debug("REST request to get BankAccount : {}", id);
        Optional<BankAccount> bankAccount = bankAccountRepository.findByIdOptional(id);
        return ResponseUtil.wrapOrNotFound(bankAccount);
    }

    /**
     * {@code DELETE  /bank-accounts/:id} : delete the "id" bankAccount.
     *
     * @param id the id of the bankAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    public Response deleteBankAccount(@PathParam("id") Long id) {
        log.debug("REST request to delete BankAccount : {}", id);
        bankAccountRepository.deleteById(id);
        Response.ResponseBuilder response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }
}
