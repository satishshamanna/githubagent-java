package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * Version Number: 1.0.0
 * User Story Number: US01
 * Date & Time of Change: 2026-07-13T07:57:32+05:30
 * User Name: Satish
 * Brief Description of Change: Created AppointmentController for scheduling vet appointments, validation, and slot querying.
 */
@Controller
public class AppointmentController {

    private final ClinicService clinicService;

    public AppointmentController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @ModelAttribute("appointment")
    public Appointment loadPetWithAppointment(@PathVariable("petId") int petId) {
        Pet pet = this.clinicService.findPetById(petId);
        Appointment appt = new Appointment();
        appt.setPet(pet);
        return appt;
    }

    @ModelAttribute("specialties")
    public Collection<String> getSpecialties() {
        return this.clinicService.findVets().stream()
            .flatMap(vet -> vet.getSpecialties().stream())
            .map(Specialty::getName)
            .distinct()
            .sorted()
            .toList();
    }

    @ModelAttribute("vets")
    public Collection<Vet> getVets() {
        return this.clinicService.findVets();
    }

    @GetMapping(value = "/owners/{ownerId}/pets/{petId}/appointments/new")
    public String initNewAppointmentForm(@PathVariable("petId") int petId, Map<String, Object> model) {
        return "appointments/createOrUpdateAppointmentForm";
    }

    @PostMapping(value = "/owners/{ownerId}/pets/{petId}/appointments/new")
    public String processNewAppointmentForm(
            @Valid @ModelAttribute("appointment") Appointment appointment,
            BindingResult result,
            @PathVariable("ownerId") int ownerId,
            @PathVariable("petId") int petId) {
        
        Pet pet = this.clinicService.findPetById(petId);
        appointment.setPet(pet);

        // Validation 1: Appointment date must be in the future
        if (appointment.getDate() != null && !appointment.getDate().isAfter(LocalDate.now())) {
            result.rejectValue("date", "futureDate", "Appointment date must be in the future.");
        }

        // Validation 2: Vet must not be double booked on date and timeslot
        if (appointment.getVet() != null && appointment.getDate() != null && appointment.getTimeSlot() != null) {
            Collection<Appointment> existing = this.clinicService.findAppointmentsByVetIdAndDate(
                appointment.getVet().getId(), appointment.getDate());
            boolean doubleBooked = existing.stream()
                .anyMatch(a -> appointment.getTimeSlot().equals(a.getTimeSlot()));
            if (doubleBooked) {
                result.reject("doubleBooking", "This veterinarian is already booked for the selected time slot.");
            }
        }

        if (result.hasErrors()) {
            return "appointments/createOrUpdateAppointmentForm";
        }

        this.clinicService.saveAppointment(appointment);
        return "redirect:/owners/" + ownerId;
    }

    @GetMapping(value = "/appointments/booked-slots", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Collection<String> getBookedSlots(@RequestParam("vetId") int vetId, @RequestParam("date") String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            return this.clinicService.findAppointmentsByVetIdAndDate(vetId, date).stream()
                .map(Appointment::getTimeSlot)
                .toList();
        } catch (Exception e) {
            return java.util.Collections.emptyList();
        }
    }
}


