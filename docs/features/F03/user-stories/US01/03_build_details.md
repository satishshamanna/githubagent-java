# US01 Construction: Invoice Generation & Management (Staff Flow)

The implementation details for invoice generation, repository setups, service mapping, and receptionist views.

## 1. Components Reference

### Database Configuration
- **[MODIFY]** [schema.sql](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/resources/db/h2/schema.sql)
  - Added `invoices` table and dropped if exists.
  - Linked to BDD Scenario 1 & 2 for persistent storage.
- **[MODIFY]** [data.sql](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/resources/db/h2/data.sql)
  - Seeded initial outstanding/paid invoice records for testing.
  - Linked to BDD Scenario 3 (invoice list dashboard verification).

### Model / Domain Layer
- **[NEW]** [Invoice.java](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/java/org/springframework/samples/petclinic/model/Invoice.java)
  - Core domain entity with JPA annotations mapping to H2 schema.
  - Validates owner selection (not null), amount (not null), issue/due dates, and description (not empty).

### Repository Layer
- **[NEW]** [InvoiceRepository.java](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/java/org/springframework/samples/petclinic/repository/InvoiceRepository.java)
  - Central interface defining database queries.
- **[NEW]** [JpaInvoiceRepositoryImpl.java](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaInvoiceRepositoryImpl.java)
  - JPA-based repository using EntityManager. Active under `jpa` profile.
- **[NEW]** [SpringDataInvoiceRepository.java](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataInvoiceRepository.java)
  - Spring Data JPA interface extending Repository. Active under `spring-data-jpa` profile.
- **[NEW]** [JdbcInvoiceRepositoryImpl.java](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcInvoiceRepositoryImpl.java)
  - JDBC-based repository using SimpleJdbcInsert and JdbcClient. Active under `jdbc` profile.

### Service Layer
- **[MODIFY]** [ClinicService.java](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/java/org/springframework/samples/petclinic/service/ClinicService.java)
  - Added methods for saving invoices, finding invoices by ID or owner ID, and retrieving all invoices.
- **[MODIFY]** [ClinicServiceImpl.java](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java)
  - Injected `InvoiceRepository` and implemented the transactional methods.

### Web Controller and Binder Layer
- **[NEW]** [InvoiceController.java](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/java/org/springframework/samples/petclinic/web/InvoiceController.java)
  - Handles initialization form (GET) and submission process (POST).
  - Implements validation logic: positive amount verification (AC-1.3, Scenario 2) and due date validation.

### UI / View Layer
- **[MODIFY]** [menu.tag](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/webapp/WEB-INF/tags/menu.tag)
  - Added a "Billing" navigation menu item linking to `/admin/invoices` (AC-1.1).
- **[NEW]** [invoiceList.jsp](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/webapp/WEB-INF/jsp/invoices/invoiceList.jsp)
  - Staff billing dashboard view listing all system invoices sorted by issue date (AC-1.4, Scenario 3).
- **[NEW]** [invoiceForm.jsp](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/src/main/webapp/WEB-INF/jsp/invoices/invoiceForm.jsp)
  - User creation form featuring dynamic owner selection dropdown and flatpickr datepickers (AC-1.2, AC-1.3).
