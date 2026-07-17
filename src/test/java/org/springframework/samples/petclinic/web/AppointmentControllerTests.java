package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Version Number: 1.0.0
 * User Story Number: US01
 * Date & Time of Change: 2026-07-13T07:57:32+05:30
 * User Name: Satish
 * Brief Description of Change: Created AppointmentControllerTests verifying GET, POST, validation, and AJAX endpoints.
 */
@SpringJUnitWebConfig(locations = {"classpath:spring/mvc-test-config.xml", "classpath:spring/mvc-core-config.xml"})
class AppointmentControllerTests {

    private static final int TEST_OWNER_ID = 1;
    private static final int TEST_PET_ID = 1;
    private static final int TEST_VET_ID = 2;

    @Autowired
    private AppointmentController appointmentController;

    @Autowired
    private ClinicService clinicService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(appointmentController).build();

        Pet pet = new Pet();
        pet.setId(TEST_PET_ID);
        given(this.clinicService.findPetById(TEST_PET_ID)).willReturn(pet);

        Vet vet = new Vet();
        vet.setId(TEST_VET_ID);
        given(this.clinicService.findVets()).willReturn(Collections.singletonList(vet));
    }

    @Test
    void testInitNewAppointmentForm() throws Exception {
        mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/appointments/new", TEST_OWNER_ID, TEST_PET_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("appointment"))
            .andExpect(view().name("appointments/createOrUpdateAppointmentForm"));
    }

    @Test
    void testProcessNewAppointmentFormSuccess() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/appointments/new", TEST_OWNER_ID, TEST_PET_ID)
            .param("date", LocalDate.now().plusDays(2).toString())
            .param("timeSlot", "10:00")
            .param("description", "Routine Checkup")
            .param("vet", String.valueOf(TEST_VET_ID))
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/owners/" + TEST_OWNER_ID));
    }

    @Test
    void testProcessNewAppointmentFormPastDateFailure() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/appointments/new", TEST_OWNER_ID, TEST_PET_ID)
            .param("date", LocalDate.now().minusDays(1).toString()) // Past date
            .param("timeSlot", "10:00")
            .param("description", "Routine Checkup")
            .param("vet", String.valueOf(TEST_VET_ID))
        )
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("appointment", "date"))
            .andExpect(view().name("appointments/createOrUpdateAppointmentForm"));
    }

    @Test
    void testProcessNewAppointmentFormDoubleBookingFailure() throws Exception {
        LocalDate futureDate = LocalDate.now().plusDays(5);
        Appointment existingAppt = new Appointment();
        existingAppt.setDate(futureDate);
        existingAppt.setTimeSlot("14:00");
        
        Vet vet = new Vet();
        vet.setId(TEST_VET_ID);
        existingAppt.setVet(vet);

        // Mock conflicting appointment in DB
        given(this.clinicService.findAppointmentsByVetIdAndDate(TEST_VET_ID, futureDate))
            .willReturn(Collections.singletonList(existingAppt));

        mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/appointments/new", TEST_OWNER_ID, TEST_PET_ID)
            .param("date", futureDate.toString())
            .param("timeSlot", "14:00") // Conflicting slot
            .param("description", "Conflicting Appointment")
            .param("vet", String.valueOf(TEST_VET_ID))
        )
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("appointment")) // Global error
            .andExpect(view().name("appointments/createOrUpdateAppointmentForm"));
    }

    @Test
    void testGetBookedSlots() throws Exception {
        LocalDate futureDate = LocalDate.now().plusDays(5);
        Appointment existingAppt = new Appointment();
        existingAppt.setDate(futureDate);
        existingAppt.setTimeSlot("11:00");
        
        Vet vet = new Vet();
        vet.setId(TEST_VET_ID);
        existingAppt.setVet(vet);

        given(this.clinicService.findAppointmentsByVetIdAndDate(TEST_VET_ID, futureDate))
            .willReturn(Collections.singletonList(existingAppt));

        mockMvc.perform(get("/appointments/booked-slots")
            .param("vetId", String.valueOf(TEST_VET_ID))
            .param("date", futureDate.toString())
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$[0]").value("11:00"));
    }
}
