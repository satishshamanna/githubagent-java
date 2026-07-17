package org.springframework.samples.petclinic.model;

import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Version Number: 1.0.0
 * User Story Number: US01
 * Date & Time of Change: 2026-07-13T07:57:32+05:30
 * User Name: Satish
 * Brief Description of Change: Created Appointment JPA entity model for appointment booking flow.
 */
@Entity
@Table(name = "appointments")
public class Appointment extends BaseEntity {

    @Column(name = "appointment_date")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @NotNull(message = "Appointment date is required.")
    private LocalDate date;

    @Column(name = "time_slot")
    @NotEmpty(message = "Time slot is required.")
    private String timeSlot;

    @Column(name = "description")
    @NotEmpty(message = "Description is required.")
    private String description;

    @Column(name = "status")
    private String status = "SCHEDULED";

    @ManyToOne
    @JoinColumn(name = "vet_id")
    @NotNull(message = "Veterinarian selection is required.")
    private Vet vet;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Vet getVet() {
        return vet;
    }

    public void setVet(Vet vet) {
        this.vet = vet;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}


