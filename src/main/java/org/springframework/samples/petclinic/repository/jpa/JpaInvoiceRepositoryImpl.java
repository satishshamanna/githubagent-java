/*
 * Version Number: 1.0.0
 * User Story Number: US01
 * Date & Time of Change: 2026-07-14T13:01:40+05:30
 * User Name: Satish
 * Brief Description of Change: Created JpaInvoiceRepositoryImpl class implementing Jpa data access.
 */
package org.springframework.samples.petclinic.repository.jpa;

import java.util.Collection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.samples.petclinic.model.Invoice;
import org.springframework.samples.petclinic.repository.InvoiceRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JpaInvoiceRepositoryImpl implements InvoiceRepository {

    private final EntityManager em;

    public JpaInvoiceRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(Invoice invoice) {
        if (invoice.getId() == null) {
            this.em.persist(invoice);
        } else {
            this.em.merge(invoice);
        }
    }

    @Override
    public Invoice findById(int id) {
        return this.em.find(Invoice.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Invoice> findByOwnerId(int ownerId) {
        Query query = this.em.createQuery("SELECT i FROM Invoice i WHERE i.owner.id = :ownerId");
        query.setParameter("ownerId", ownerId);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Invoice> findAll() {
        Query query = this.em.createQuery("SELECT i FROM Invoice i ORDER BY i.issueDate DESC");
        return query.getResultList();
    }
}
