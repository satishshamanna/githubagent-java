/*
 * Version Number: 1.1.0
 * User Story Number: US01
 * Date & Time of Change: 2026-07-14T13:01:40+05:30
 * User Name: Satish
 * Brief Description of Change: Added Invoice service interface methods.
 */
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Invoice;


/**
 * Mostly used as a facade so all controllers have a single point of entry
 *
 * @author Michael Isvy
 */
public interface ClinicService {

    Collection<PetType> findPetTypes();

    Owner findOwnerById(int id);

    Pet findPetById(int id);

    void savePet(Pet pet);

    void saveVisit(Visit visit);

    Collection<Vet> findVets();

    void saveOwner(Owner owner);

    Collection<Owner> findOwnerByLastName(String lastName);

	Collection<Visit> findVisitsByPetId(int petId);

    void saveAppointment(Appointment appointment);

    Collection<Appointment> findAppointmentsByVetIdAndDate(int vetId, java.time.LocalDate date);

    Collection<Appointment> findAppointmentsByPetId(int petId);

    void saveInvoice(Invoice invoice) throws DataAccessException;

    Invoice findInvoiceById(int id) throws DataAccessException;

    Collection<Invoice> findInvoicesByOwnerId(int ownerId) throws DataAccessException;

    Collection<Invoice> findAllInvoices() throws DataAccessException;

}



