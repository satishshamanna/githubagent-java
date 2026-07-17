# 00_epic_spec.md: EPIC-03 - Clinic Billing & Invoicing

This epic defines the high-level business goals, requirements, user personas, and flow architecture for the **Clinic Billing & Invoicing** system in the Spring PetClinic application.

---

## 1. Business Value & Goals

Currently, the Spring PetClinic application tracks owner registration, pets, visits, and vet appointments, but has no mechanism for recording clinical charges or processing payments. 

Implementing a billing system achieves the following:
- **Revenue Generation**: Transforms the application into a commercial practice management system by letting vets and staff bill for services.
- **Self-Service Billing**: Enables pet owners to view their invoices, check payment due dates, and perform simulated online payments.
- **Payment Lifecycle Tracking**: Integrates billing statuses directly with clinical visits and vet appointments.

---

## 2. Target Personas

1. **Owner (Pet Parent)**:
   - Needs to view outstanding and historic invoices for their pets.
   - Needs to make simulated payments online via credit card.
2. **Clinic Staff (Receptionist/Admin)**:
   - Needs to generate invoices based on visits or appointments.
   - Needs to track unpaid/overdue invoices and manually mark cash payments.
3. **Veterinarian**:
   - Needs to specify diagnostic, treatment, and consultation fees during/after a pet visit.

---

## 3. High-Level Requirements & Scope

- **Invoice Creation**: Invoices must be generated manually by receptionists or automatically linked to a completed [Visit](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/java/org/springframework/samples/petclinic/model/Visit.java) or [Appointment](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/java/org/springframework/samples/petclinic/model/Appointment.java).
- **Payment Statuses**: Unpaid, Paid, Overdue, Voided.
- **Line Items / Costs**: Invoices contain a base consultation fee, plus additional charges for specific visits or procedures.
- **Owner Payment Portal**: An interface for owners to view details and simulate payments.

---

## 4. Epic Traceability Map

This epic spans the following features:
- **Feature F03**: Clinic Billing & Invoicing (Feature Design Spec: [01_feature_design.md](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/docs/features/F03/01_feature_design.md))
