/*
 * Version Number: 1.0.0
 * User Story Number: US01
 * Date & Time of Change: 2026-07-14T13:01:40+05:30
 * User Name: Satish
 * Brief Description of Change: Created JdbcInvoiceRepositoryImpl class implementing JDBC data access.
 */
package org.springframework.samples.petclinic.repository.jdbc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.samples.petclinic.model.Invoice;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.repository.InvoiceRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcInvoiceRepositoryImpl implements InvoiceRepository {

    private final JdbcClient jdbcClient;
    private final SimpleJdbcInsert insertInvoice;

    public JdbcInvoiceRepositoryImpl(DataSource dataSource, JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
        this.insertInvoice = new SimpleJdbcInsert(dataSource)
            .withTableName("invoices")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public void save(Invoice invoice) {
        if (invoice.isNew()) {
            Number newKey = this.insertInvoice.executeAndReturnKey(createInvoiceParameterSource(invoice));
            invoice.setId(newKey.intValue());
        } else {
            this.jdbcClient.sql("UPDATE invoices SET owner_id=:owner_id, visit_id=:visit_id, appointment_id=:appointment_id, amount=:amount, issue_date=:issue_date, due_date=:due_date, payment_status=:payment_status, payment_date=:payment_date, description=:description WHERE id=:id")
                .param("owner_id", invoice.getOwner().getId())
                .param("visit_id", invoice.getVisit() != null ? invoice.getVisit().getId() : null)
                .param("appointment_id", invoice.getAppointment() != null ? invoice.getAppointment().getId() : null)
                .param("amount", invoice.getAmount())
                .param("issue_date", invoice.getIssueDate())
                .param("due_date", invoice.getDueDate())
                .param("payment_status", invoice.getPaymentStatus())
                .param("payment_date", invoice.getPaymentDate())
                .param("description", invoice.getDescription())
                .param("id", invoice.getId())
                .update();
        }
    }

    private MapSqlParameterSource createInvoiceParameterSource(Invoice invoice) {
        return new MapSqlParameterSource()
            .addValue("id", invoice.getId())
            .addValue("owner_id", invoice.getOwner().getId())
            .addValue("visit_id", invoice.getVisit() != null ? invoice.getVisit().getId() : null)
            .addValue("appointment_id", invoice.getAppointment() != null ? invoice.getAppointment().getId() : null)
            .addValue("amount", invoice.getAmount())
            .addValue("issue_date", invoice.getIssueDate())
            .addValue("due_date", invoice.getDueDate())
            .addValue("payment_status", invoice.getPaymentStatus())
            .addValue("payment_date", invoice.getPaymentDate())
            .addValue("description", invoice.getDescription());
    }

    @Override
    public Invoice findById(int id) {
        return this.jdbcClient.sql("SELECT id, owner_id, visit_id, appointment_id, amount, issue_date, due_date, payment_status, payment_date, description FROM invoices WHERE id=:id")
            .param("id", id)
            .query((rs, rowNum) -> {
                Invoice i = new Invoice();
                i.setId(rs.getInt("id"));
                i.setAmount(rs.getBigDecimal("amount"));
                i.setIssueDate(rs.getObject("issue_date", LocalDate.class));
                i.setDueDate(rs.getObject("due_date", LocalDate.class));
                i.setPaymentStatus(rs.getString("payment_status"));
                i.setPaymentDate(rs.getObject("payment_date", LocalDate.class));
                i.setDescription(rs.getString("description"));

                Owner owner = new Owner();
                owner.setId(rs.getInt("owner_id"));
                i.setOwner(owner);

                int visitId = rs.getInt("visit_id");
                if (!rs.wasNull()) {
                    Visit visit = new Visit();
                    visit.setId(visitId);
                    i.setVisit(visit);
                }

                int appointmentId = rs.getInt("appointment_id");
                if (!rs.wasNull()) {
                    Appointment appt = new Appointment();
                    appt.setId(appointmentId);
                    i.setAppointment(appt);
                }

                return i;
            })
            .single();
    }

    @Override
    public Collection<Invoice> findByOwnerId(int ownerId) {
        return this.jdbcClient.sql("SELECT id, owner_id, visit_id, appointment_id, amount, issue_date, due_date, payment_status, payment_date, description FROM invoices WHERE owner_id=:ownerId")
            .param("ownerId", ownerId)
            .query((rs, rowNum) -> {
                Invoice i = new Invoice();
                i.setId(rs.getInt("id"));
                i.setAmount(rs.getBigDecimal("amount"));
                i.setIssueDate(rs.getObject("issue_date", LocalDate.class));
                i.setDueDate(rs.getObject("due_date", LocalDate.class));
                i.setPaymentStatus(rs.getString("payment_status"));
                i.setPaymentDate(rs.getObject("payment_date", LocalDate.class));
                i.setDescription(rs.getString("description"));

                Owner owner = new Owner();
                owner.setId(rs.getInt("owner_id"));
                i.setOwner(owner);

                int visitId = rs.getInt("visit_id");
                if (!rs.wasNull()) {
                    Visit visit = new Visit();
                    visit.setId(visitId);
                    i.setVisit(visit);
                }

                int appointmentId = rs.getInt("appointment_id");
                if (!rs.wasNull()) {
                    Appointment appt = new Appointment();
                    appt.setId(appointmentId);
                    i.setAppointment(appt);
                }

                return i;
            })
            .list();
    }

    @Override
    public Collection<Invoice> findAll() {
        return this.jdbcClient.sql("SELECT id, owner_id, visit_id, appointment_id, amount, issue_date, due_date, payment_status, payment_date, description FROM invoices ORDER BY issue_date DESC")
            .query((rs, rowNum) -> {
                Invoice i = new Invoice();
                i.setId(rs.getInt("id"));
                i.setAmount(rs.getBigDecimal("amount"));
                i.setIssueDate(rs.getObject("issue_date", LocalDate.class));
                i.setDueDate(rs.getObject("due_date", LocalDate.class));
                i.setPaymentStatus(rs.getString("payment_status"));
                i.setPaymentDate(rs.getObject("payment_date", LocalDate.class));
                i.setDescription(rs.getString("description"));

                Owner owner = new Owner();
                owner.setId(rs.getInt("owner_id"));
                i.setOwner(owner);

                int visitId = rs.getInt("visit_id");
                if (!rs.wasNull()) {
                    Visit visit = new Visit();
                    visit.setId(visitId);
                    i.setVisit(visit);
                }

                int appointmentId = rs.getInt("appointment_id");
                if (!rs.wasNull()) {
                    Appointment appt = new Appointment();
                    appt.setId(appointmentId);
                    i.setAppointment(appt);
                }

                return i;
            })
            .list();
    }
}
