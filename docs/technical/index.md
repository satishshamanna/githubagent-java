# Technical Reference Index

This registry tracks the active APIs, schema mappings, and core packages in the Spring PetClinic application.

## System Map

### 1. Model Domain Packages
* **Path**: `org.springframework.samples.petclinic.model`
* **Core Entities**:
  - `BaseEntity`
  - `NamedEntity`
  - `Owner`
  - `Pet`
  - `PetType`
  - `Specialty`
  - `Vet`
  - `Visit`
  - `Appointment` ([F01 Design](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/docs/features/F01/01_feature_design.md))

### 2. Repository Persistence Mappings
* **Path**: `org.springframework.samples.petclinic.repository`
* **Implementations**: JPA, JDBC, and Spring Data JPA profiles.
* **Core Repositories**:
  - `OwnerRepository`
  - `PetRepository`
  - `VetRepository`
  - `VisitRepository`
  - `AppointmentRepository` ([F01 Design](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/docs/features/F01/01_feature_design.md))

### 3. Controller API Layer
* **Path**: `org.springframework.samples.petclinic.web`
* **Active Endpoints**:
  - `GET /vets` (Vet listing)
  - `GET /owners/new`, `POST /owners/new` (Owner creation)
  - `GET /owners/{ownerId}` (Owner details)
  - `GET /owners/{ownerId}/pets/new`, `POST /owners/{ownerId}/pets/new` (Pet creation)
  - `GET /owners/{ownerId}/pets/{petId}/visits/new`, `POST /owners/{ownerId}/pets/{petId}/visits/new` (Visit logging)
  - `GET /owners/{ownerId}/pets/{petId}/appointments/new`, `POST /owners/{ownerId}/pets/{petId}/appointments/new` (F01 Appointment Scheduling)
