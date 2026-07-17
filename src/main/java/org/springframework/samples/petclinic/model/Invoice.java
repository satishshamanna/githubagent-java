/*
 * Version Number: 1.0.0
 * User Story Number: US01
 * Date & Time of Change: 2026-07-14T13:01:40+05:30
 * User Name: Satish
 * Brief Description of Change: Created Invoice JPA entity model.
 */
package org.springframework.samples.petclinic.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "invoices")
public class Invoice extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @NotNull(message = "Owner selection is required.")
    private Owner owner;

    @ManyToOne
    @JoinColumn(name = "visit_id")
    private Visit visit;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(name = "amount")
    @NotNull(message = "Amount is required.")
    private BigDecimal amount;

    @Column(name = "issue_date")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @NotNull(message = "Issue date is required.")
    private LocalDate issueDate;

    @Column(name = "due_date")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @NotNull(message = "Due date is required.")
    private LocalDate dueDate;

    @Column(name = "payment_status")
    private String paymentStatus = "UNPAID";

    @Column(name = "payment_date")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate paymentDate;

    @Column(name = "description")
    @NotEmpty(message = "Description is required.")
    private String description;

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
