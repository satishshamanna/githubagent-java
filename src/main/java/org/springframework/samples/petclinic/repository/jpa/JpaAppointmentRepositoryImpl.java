package org.springframework.samples.petclinic.repository.jpa;

import java.time.LocalDate;
import java.util.Collection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.repository.AppointmentRepository;
import org.springframework.stereotype.Repository;

/**
 * Version Number: 1.0.0
 * User Story Number: US01
 * Date & Time of Change: 2026-07-13T07:57:32+05:30
 * User Name: Satish
 * Brief Description of Change: Created JpaAppointmentRepositoryImpl class implementing Jpa data access.
 */
@Repository
public class JpaAppointmentRepositoryImpl implements AppointmentRepository {

    private final EntityManager em;

    public JpaAppointmentRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(Appointment appointment) {
        if (appointment.getId() == null) {
            this.em.persist(appointment);
        } else {
            this.em.merge(appointment);
        }
    }

    @Override
    public Appointment findById(int id) {
        return this.em.find(Appointment.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Appointment> findByVetIdAndDate(int vetId, LocalDate date) {
        Query query = this.em.createQuery("SELECT a FROM Appointment a WHERE a.vet.id = :vetId AND a.date = :date");
        query.setParameter("vetId", vetId);
        query.setParameter("date", date);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Appointment> findByPetId(int petId) {
        Query query = this.em.createQuery("SELECT a FROM Appointment a WHERE a.pet.id = :petId");
        query.setParameter("petId", petId);
        return query.getResultList();
    }
}


