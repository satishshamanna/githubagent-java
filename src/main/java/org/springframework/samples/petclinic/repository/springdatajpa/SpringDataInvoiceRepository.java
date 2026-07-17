/*
 * Version Number: 1.0.0
 * User Story Number: US01
 * Date & Time of Change: 2026-07-14T13:01:40+05:30
 * User Name: Satish
 * Brief Description of Change: Created SpringDataInvoiceRepository interface extending Spring Data Repository.
 */
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Invoice;
import org.springframework.samples.petclinic.repository.InvoiceRepository;

public interface SpringDataInvoiceRepository extends InvoiceRepository, Repository<Invoice, Integer> {

    @Override
    @Query("SELECT i FROM Invoice i WHERE i.owner.id = :ownerId")
    Collection<Invoice> findByOwnerId(@Param("ownerId") int ownerId);

    @Override
    @Query("SELECT i FROM Invoice i ORDER BY i.issueDate DESC")
    Collection<Invoice> findAll();
}
