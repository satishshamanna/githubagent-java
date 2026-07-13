
# US01 Construction: Book Vet Appointment (Owner Flow)

The implementation details for scheduling vet appointments.

## 1. Components Reference

### Database Configuration
- **[MODIFY]** [schema.sql](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/resources/db/h2/schema.sql)
  - Added `appointments` table and dropped if exists.
  - Linked to BDD Scenario 1 & 2 for persistent storage.
- **[MODIFY]** [data.sql](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/resources/db/h2/data.sql)
  - Added initial seed appointment data for vet 2 (Helen Leary) on 2026-08-20 at 14:00.
  - Linked to BDD Scenario 2 (double booking check).

### Model / Domain Layer
- **[NEW]** [Appointment.java](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/java/org/springframework/samples/petclinic/model/Appointment.java)
  - Core domain entity with JPA annotations mapping to H2 schema.
  - Validates date (not null), time slot, description, and veterinarian.

### Repository Layer
- **[NEW]** [AppointmentRepository.java](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/java/org/springframework/samples/petclinic/repository/AppointmentRepository.java)
  - Central interface defining database queries.
- **[NEW]** [JpaAppointmentRepositoryImpl.java](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaAppointmentRepositoryImpl.java)
  - JPA-based repository using EntityManager. Active under `jpa` profile.
- **[NEW]** [SpringDataAppointmentRepository.java](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataAppointmentRepository.java)
  - Spring Data JPA interface extending Repository. Active under `spring-data-jpa` profile.
- **[NEW]** [JdbcAppointmentRepositoryImpl.java](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcAppointmentRepositoryImpl.java)
  - JDBC-based repository using SimpleJdbcInsert and JdbcClient. Active under `jdbc` profile.

### Service Layer
- **[MODIFY]** [ClinicService.java](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/java/org/springframework/samples/petclinic/service/ClinicService.java)
  - Added methods for saving appointments and querying them by pet/vet.
- **[MODIFY]** [ClinicServiceImpl.java](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java)
  - Injected `AppointmentRepository` and implemented the transactional methods.

### Web Controller and Binder Layer
- **[NEW]** [VetFormatter.java](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/java/org/springframework/samples/petclinic/web/VetFormatter.java)
  - Custom Spring Formatter registered under `conversionService` in mvc-core-config.xml. Parses Vet IDs into Vet model objects.
- **[MODIFY]** [mvc-core-config.xml](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/resources/spring/mvc-core-config.xml)
  - Registered `VetFormatter` bean.
- **[NEW]** [AppointmentController.java](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/java/org/springframework/samples/petclinic/web/AppointmentController.java)
  - Handles initialization form (GET) and submission process (POST).
  - Implements REST endpoint `/appointments/booked-slots` to fetch booked slots.
  - Implements validation logic: future date verification (AC-1.5, Scenario 3) and double booking prevention (AC-1.4, Scenario 2).

### UI / View Layer
- **[MODIFY]** [ownerDetails.jsp](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp)
  - Placed "Book Appointment" link next to "Add Visit" button for each pet (AC-1.1).
- **[NEW]** [createOrUpdateAppointmentForm.jsp](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/webapp/WEB-INF/jsp/appointments/createOrUpdateAppointmentForm.jsp)
  - User scheduling form featuring flatpickr datepicker, specialty-based vet filtering via Javascript, and AJAX-driven time slot selector disabling booked slots (AC-1.2, AC-1.3).

