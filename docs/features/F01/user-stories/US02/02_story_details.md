# US02: Veterinarian Appointment Dashboard & Management (Vet Flow)

* **Story Point Estimate**: 2 (Fibonacci)
* **Parent Feature**: [F01 - Appointment Scheduling & Vet Availability](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/docs/features/F01/01_feature_design.md)

---

## 1. Description
**As a** Veterinarian  
**I want to** view my upcoming appointment schedule and update the status of scheduled visits  
**So that** I can manage my clinical hours and pet assignments productively.

---

## 2. Acceptance Criteria

| ID | Given | When | Then |
|---|---|---|---|
| AC-2.1 | Vet accesses the dashboard route `/vets/{vetId}/appointments` | The page loads | Displays a list of appointments for that Vet, sorted chronologically by date and time slot. |
| AC-2.2 | Vet is on their schedule page | They inspect the table list | Each appointment displays: Pet Name, Owner Name, Date, Time Slot, Description, and Status. |
| AC-2.3 | Vet views a "SCHEDULED" appointment | They click the "Complete" button next to it | The status updates to "COMPLETED" in the database, and the page refreshes to display the updated status. |
| AC-2.4 | Vet views a "SCHEDULED" appointment | They click the "Cancel" button next to it | The status updates to "CANCELLED" in the database, and the page refreshes to display the updated status. |

---

## 3. BDD Gherkin Scenarios

### Scenario 1: Vet views their scheduled appointments
```gherkin
Given veterinarian "Rafael Ortega" has 2 scheduled appointments for "2026-08-25"
When "Rafael Ortega" navigates to "/vets/3/appointments"
Then they see a table containing 2 rows
And the columns display the details for pet "Max" (owner "Jean Coleman") and pet "Samantha" (owner "Jean Coleman")
```

### Scenario 2: Vet completes an appointment
```gherkin
Given an appointment for pet "Max" with Vet "Rafael Ortega" on "2026-08-25" at "10:00" is in the state "SCHEDULED"
When veterinarian "Rafael Ortega" clicks "Complete" on this appointment
Then the appointment status is updated to "COMPLETED"
And the page refreshes showing the status "COMPLETED" for that appointment row
```

---

## 4. TDD Test Outlines

We will write unit and controller tests using **JUnit 5** and **Mockito**.

### Backend JUnit Test Skeletons

#### 1. Controller Dashboard Retrieval Test (`AppointmentControllerTests.java` addition)
```java
    @Test
    void testShowVetDashboard() throws Exception {
        mockMvc.perform(get("/vets/1/appointments"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("appointmentsList"))
            .andExpect(view().name("vets/vetDashboard"));
    }

    @Test
    void testUpdateAppointmentStatus() throws Exception {
        mockMvc.perform(post("/vets/1/appointments/1/status")
            .param("status", "COMPLETED"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/vets/1/appointments"));
    }
```

---

## 5. Manual Verification Plan

| Step # | Action | Expected Output |
|---|---|---|
| 1 | Book an appointment with Vet "Helen Leary" (ID: 2) for next Tuesday at 14:00 (following US01 flow). | Booking completes successfully. |
| 2 | Navigate to `/vets/2/appointments` (Helen Leary's dashboard). | Schedule list displays a row for the pet, date, time "14:00", description, and status "SCHEDULED". |
| 3 | Click "Complete" button next to the appointment. | Redirection refreshes page. The status for that appointment is now updated to "COMPLETED". |
| 4 | Check the "Complete" and "Cancel" buttons for the completed appointment row. | Buttons are disabled or hidden, indicating the status is final. |

---

## 6. Technical Components Reference

- **Model Entity**: `org.springframework.samples.petclinic.model.Appointment` **[EXISTING]** (defined in US01)
- **Repository**: `org.springframework.samples.petclinic.repository.AppointmentRepository` **[EXISTING]** (defined in US01)
- **Controller**: `org.springframework.samples.petclinic.web.AppointmentController` **[EXISTING]** (add routes for dashboard & status update)
- **UI Dashboard View**: `src/main/webapp/WEB-INF/jsp/vets/vetDashboard.jsp` **[NEW]** (JSP view for schedule dashboard)
- **Link Placement**: `src/main/webapp/WEB-INF/jsp/vets/vetList.jsp` or navigation bar **[MODIFY]** (adds link to view dashboard)
