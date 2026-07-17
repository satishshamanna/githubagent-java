package org.springframework.samples.petclinic.repository.jdbc;

import java.time.LocalDate;
import java.util.Collection;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.AppointmentRepository;
import org.springframework.stereotype.Repository;

/**
 * Version Number: 1.0.0
 * User Story Number: US01
 * Date & Time of Change: 2026-07-13T07:57:32+05:30
 * User Name: Satish
 * Brief Description of Change: Created JdbcAppointmentRepositoryImpl class implementing JDBC data access.
 */
@Repository
public class JdbcAppointmentRepositoryImpl implements AppointmentRepository {

    private final JdbcClient jdbcClient;
    private final SimpleJdbcInsert insertAppointment;

    public JdbcAppointmentRepositoryImpl(DataSource dataSource, JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
        this.insertAppointment = new SimpleJdbcInsert(dataSource)
            .withTableName("appointments")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public void save(Appointment appointment) {
        if (appointment.isNew()) {
            Number newKey = this.insertAppointment.executeAndReturnKey(
                createAppointmentParameterSource(appointment));
            appointment.setId(newKey.intValue());
        } else {
            this.jdbcClient.sql("UPDATE appointments SET pet_id=:pet_id, vet_id=:vet_id, appointment_date=:appointment_date, time_slot=:time_slot, description=:description, status=:status WHERE id=:id")
                .param("pet_id", appointment.getPet().getId())
                .param("vet_id", appointment.getVet().getId())
                .param("appointment_date", appointment.getDate())
                .param("time_slot", appointment.getTimeSlot())
                .param("description", appointment.getDescription())
                .param("status", appointment.getStatus())
                .param("id", appointment.getId())
                .update();
        }
    }

    private MapSqlParameterSource createAppointmentParameterSource(Appointment appointment) {
        return new MapSqlParameterSource()
            .addValue("id", appointment.getId())
            .addValue("pet_id", appointment.getPet().getId())
            .addValue("vet_id", appointment.getVet().getId())
            .addValue("appointment_date", appointment.getDate())
            .addValue("time_slot", appointment.getTimeSlot())
            .addValue("description", appointment.getDescription())
            .addValue("status", appointment.getStatus());
    }

    @Override
    public Appointment findById(int id) {
        return this.jdbcClient.sql("SELECT id, pet_id, vet_id, appointment_date, time_slot, description, status FROM appointments WHERE id=:id")
            .param("id", id)
            .query((rs, rowNum) -> {
                Appointment a = new Appointment();
                a.setId(rs.getInt("id"));
                a.setDate(rs.getObject("appointment_date", LocalDate.class));
                a.setTimeSlot(rs.getString("time_slot"));
                a.setDescription(rs.getString("description"));
                a.setStatus(rs.getString("status"));
                
                Pet pet = new Pet();
                pet.setId(rs.getInt("pet_id"));
                a.setPet(pet);
                
                Vet vet = new Vet();
                vet.setId(rs.getInt("vet_id"));
                a.setVet(vet);
                
                return a;
            })
            .single();
    }

    @Override
    public Collection<Appointment> findByVetIdAndDate(int vetId, LocalDate date) {
        return this.jdbcClient.sql("SELECT id, pet_id, vet_id, appointment_date, time_slot, description, status FROM appointments WHERE vet_id=:vetId AND appointment_date=:date")
            .param("vetId", vetId)
            .param("date", date)
            .query((rs, rowNum) -> {
                Appointment a = new Appointment();
                a.setId(rs.getInt("id"));
                a.setDate(rs.getObject("appointment_date", LocalDate.class));
                a.setTimeSlot(rs.getString("time_slot"));
                a.setDescription(rs.getString("description"));
                a.setStatus(rs.getString("status"));
                
                Pet pet = new Pet();
                pet.setId(rs.getInt("pet_id"));
                a.setPet(pet);
                
                Vet vet = new Vet();
                vet.setId(rs.getInt("vet_id"));
                a.setVet(vet);
                
                return a;
            })
            .list();
    }

    @Override
    public Collection<Appointment> findByPetId(int petId) {
        return this.jdbcClient.sql("SELECT id, pet_id, vet_id, appointment_date, time_slot, description, status FROM appointments WHERE pet_id=:petId")
            .param("petId", petId)
            .query((rs, rowNum) -> {
                Appointment a = new Appointment();
                a.setId(rs.getInt("id"));
                a.setDate(rs.getObject("appointment_date", LocalDate.class));
                a.setTimeSlot(rs.getString("time_slot"));
                a.setDescription(rs.getString("description"));
                a.setStatus(rs.getString("status"));
                
                Pet pet = new Pet();
                pet.setId(rs.getInt("pet_id"));
                a.setPet(pet);
                
                Vet vet = new Vet();
                vet.setId(rs.getInt("vet_id"));
                a.setVet(vet);
                
                return a;
            })
            .list();
    }
}


