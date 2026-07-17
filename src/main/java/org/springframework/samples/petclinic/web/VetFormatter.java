package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;
import org.jspecify.annotations.NullMarked;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.ClinicService;

/**
 * Version Number: 1.0.0
 * User Story Number: US01
 * Date & Time of Change: 2026-07-13T07:57:32+05:30
 * User Name: Satish
 * Brief Description of Change: Created VetFormatter class to bind Vet entities from request parameters.
 */
@NullMarked
public class VetFormatter implements Formatter<Vet> {

    private final ClinicService clinicService;

    public VetFormatter(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @Override
    public String print(Vet vet, Locale locale) {
        return (vet.getId() != null) ? vet.getId().toString() : "";
    }

    @Override
    public Vet parse(String text, Locale locale) throws ParseException {
        try {
            int id = Integer.parseInt(text);
            Collection<Vet> findVets = this.clinicService.findVets();
            for (Vet vet : findVets) {
                if (vet.getId() != null && vet.getId() == id) {
                    return vet;
                }
            }
        } catch (NumberFormatException e) {
            throw new ParseException("Invalid vet ID: " + text, 0);
        }
        throw new ParseException("vet not found: " + text, 0);
    }
}


