# US01: Book Vet Appointment (Owner Flow)

* **Story Point Estimate**: 3 (Fibonacci)
* **Parent Feature**: [F01 - Appointment Scheduling & Vet Availability](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/docs/features/F01/01_feature_design.md)

---

## 1. Description
**As a** Pet Owner  
**I want to** schedule an appointment for my pet with a specific veterinarian on an available date and time slot  
**So that** my pet can receive timely and specialized care.

---

## 2. Acceptance Criteria

| ID | Given | When | Then |
|---|---|---|---|
| AC-1.1 | Owner is on the Owner Details page | They click "Book Appointment" next to a pet | They are redirected to the appointment scheduling form with the pet pre-selected. |
| AC-1.2 | Owner is on the scheduling form | They select a Specialty | The Vet dropdown is filtered to display only veterinarians with that specialty. |
| AC-1.3 | Owner selects a Vet and Date | They select a Time Slot | The slot selector shows available hours, and disables any slots that are already booked for that Vet on that Date. |
| AC-1.4 | Owner attempts to book | The selected Vet/Date/Slot is already booked | The system rejects the booking, displays a validation error message: "This veterinarian is already booked for the selected time slot.", and keeps the form open. |
| AC-1.5 | Owner selects a Date | The selected date is today or in the past | The system rejects the booking, displaying a validation error message: "Appointment date must be in the future." |

---

## 3. BDD Gherkin Scenarios

### Scenario 1: Successfully book a veterinarian appointment
```gherkin
Given the owner is booking an appointment for pet "Leo"
And veterinarian "James Carter" (Specialty: "radiology") has an available slot at "10:00" on "2026-08-15"
When the owner schedules the appointment with "James Carter" at "10:00" on "2026-08-15" with description "Yearly checkup"
Then the appointment is successfully registered in the status "SCHEDULED"
And the owner is redirected to the Owner Details page with a success message
```

### Scenario 2: Prevent double booking of a veterinarian slot
```gherkin
Given veterinarian "Helen Leary" (Specialty: "surgery") is already booked at "14:00" on "2026-08-20"
When another owner attempts to schedule an appointment with "Helen Leary" at "14:00" on "2026-08-20"
Then the booking is rejected with a validation error
And the appointment is not registered in the database
```

### Scenario 3: Prevent scheduling appointments in the past
```gherkin
Given the current date is "2026-07-13"
When the owner attempts to schedule an appointment for "2026-07-12" at "11:00"
Then the booking is rejected with a validation error: "Appointment date must be in the future."
```

---

## 4. TDD Test Outlines

We will write unit and slice integration tests using **JUnit 5** and **Mockito**.

### Backend JUnit Test Skeletons

#### 1. Repository Tests (`AppointmentRepositoryTests.java`)
```java
package org.springframework.samples.petclinic.repository;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.model.Appointment;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AppointmentRepositoryTests {

    @Autowired
    private AppointmentRepository appointments;

    @Test
    void shouldInsertAndFindAppointment() {
        Appointment appt = new Appointment();
        appt.setDate(LocalDate.of(2026, 8, 15));
        appt.setTimeSlot("10:00");
        appt.setDescription("Checkup");
        
        // Save
        this.appointments.save(appt);
        
        // Retrieve
        Appointment retrieved = this.appointments.findById(appt.getId());
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getTimeSlot()).isEqualTo("10:00");
    }
}
```

#### 2. Controller Validation Tests (`AppointmentControllerTests.java`)
```java
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.samples.petclinic.repository.AppointmentRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentRepository appointments;

    @Test
    void testProcessCreationFormSuccess() throws Exception {
        mockMvc.perform(post("/owners/1/pets/1/appointments/new")
            .param("date", "2026-09-01")
            .param("timeSlot", "11:00")
            .param("description", "Routine vaccine check")
            .param("vet", "1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/owners/1"));
    }

    @Test
    void testProcessCreationFormPastDateFailure() throws Exception {
        mockMvc.perform(post("/owners/1/pets/1/appointments/new")
            .param("date", "2025-01-01") // Past date
            .param("timeSlot", "11:00")
            .param("description", "Routine vaccine check")
            .param("vet", "1"))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("appointment"))
            .andExpect(model().attributeHasFieldErrors("appointment", "date"))
            .andExpect(view().name("appointments/createOrUpdateAppointmentForm"));
    }
}
```

---

## 5. Manual Verification Plan

| Step # | Action | Expected Output |
|---|---|---|
| 1 | Navigate to `/owners/1` (details of Owner George Franklin). | Owner Details page displays George's pet "Leo". A "Book Appointment" button is visible next to Leo. |
| 2 | Click on "Book Appointment". | Redirects to `/owners/1/pets/1/appointments/new` displaying the booking form. |
| 3 | Input a past date (e.g. `2026-01-01`), enter description, select Vet, and click "Submit". | Page reloads showing validation error: "Appointment date must be in the future." |
| 4 | Input a future date, select Specialty "dentistry", select Vet "Rafael Ortega", choose time slot "09:00", enter description, and click "Submit". | Redirects to Owner page `/owners/1`. Success flash message is shown. |
| 5 | Repeat step 2, input the exact same date and Vet, and select "09:00" slot again. | Form blocks or displays error on submission: "This veterinarian is already booked for the selected time slot." |

---

## 6. Technical Components Reference

- **Model Entity**: `org.springframework.samples.petclinic.model.Appointment` **[NEW]**
- **Repository**: `org.springframework.samples.petclinic.repository.AppointmentRepository` **[NEW]**
- **Controller**: `org.springframework.samples.petclinic.web.AppointmentController` **[NEW]**
- **UI Form View**: `src/main/webapp/WEB-INF/jsp/appointments/createOrUpdateAppointmentForm.jsp` **[NEW]**
- **Schema Mapping**: `src/main/resources/db/h2/schema.sql` **[MODIFY]** (registers `appointments` table)
- **Sample Seed Data**: `src/main/resources/db/h2/data.sql` **[MODIFY]** (adds initial appointments)
- **Link Placement**: `src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp` **[MODIFY]** (adds booking link)
