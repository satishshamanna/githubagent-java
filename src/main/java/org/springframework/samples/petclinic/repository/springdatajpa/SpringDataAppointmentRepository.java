package org.springframework.samples.petclinic.repository.springdatajpa;

import java.time.LocalDate;
import java.util.Collection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.repository.AppointmentRepository;

/**
 * Version Number: 1.0.0
 * User Story Number: US01
 * Date & Time of Change: 2026-07-13T07:57:32+05:30
 * User Name: Satish
 * Brief Description of Change: Created SpringDataAppointmentRepository interface extending Spring Data Repository.
 */
public interface SpringDataAppointmentRepository extends AppointmentRepository, Repository<Appointment, Integer> {

    @Override
    @Query("SELECT a FROM Appointment a WHERE a.vet.id = :vetId AND a.date = :date")
    Collection<Appointment> findByVetIdAndDate(@Param("vetId") int vetId, @Param("date") LocalDate date);

    @Override
    @Query("SELECT a FROM Appointment a WHERE a.pet.id = :petId")
    Collection<Appointment> findByPetId(@Param("petId") int petId);
}


