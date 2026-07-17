/*
 * Version Number: 1.0.0
 * User Story Number: US01
 * Date & Time of Change: 2026-07-14T13:01:40+05:30
 * User Name: Satish
 * Brief Description of Change: Created InvoiceRepository data access interface.
 */
package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Invoice;

public interface InvoiceRepository {
    void save(Invoice invoice) throws DataAccessException;
    Invoice findById(int id) throws DataAccessException;
    Collection<Invoice> findByOwnerId(int ownerId) throws DataAccessException;
    Collection<Invoice> findAll() throws DataAccessException;
}
