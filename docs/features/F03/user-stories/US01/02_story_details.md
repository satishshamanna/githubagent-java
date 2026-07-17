# US01: Invoice Generation & Management (Staff Flow)

* **Story Point Estimate**: 3 (Fibonacci)
* **Parent Feature**: [F03 - Clinic Billing & Invoicing](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/docs/features/F03/01_feature_design.md)

---

## 1. Description
**As a** Clinic Receptionist / Administrator  
**I want to** generate and record billing invoices for pet owners  
**So that** the clinic can track outstanding balances and receive payments for services.

---

## 2. Acceptance Criteria

| ID | Given | When | Then |
|---|---|---|---|
| AC-1.1 | Administrator is on the Admin Invoices dashboard | They click "Create Invoice" | They are redirected to the Invoice Creation form. |
| AC-1.2 | Administrator is on the Invoice Creation form | They fill in the Owner, Amount, Due Date, and Description and click Save | The invoice is saved with status `UNPAID` and they are redirected to the Admin dashboard. |
| AC-1.3 | Administrator is on the Invoice Creation form | They enter an Amount <= 0 or leave required fields empty | The form displays validation errors and prevents submission. |
| AC-1.4 | Administrator is on the Admin Invoices dashboard | They view the list | They see all invoices sorted by issue date (newest first) with appropriate status badges (UNPAID = Yellow/Gray, PAID = Green). |

---

## 3. BDD Gherkin Scenarios

### Scenario 1: Successfully generate invoice for an owner
```gherkin
Given the administrator is logged in and on the Invoice Creation page
When the administrator selects owner "George Franklin"
And sets the amount to "50.00"
And sets the due date to "2026-08-30"
And sets the description to "Dental cleaning and checkup"
And clicks save
Then the invoice is registered in status "UNPAID"
And the administrator is redirected to the Admin Invoices dashboard with a success message
```

### Scenario 2: Prevent invoice creation with invalid amount
```gherkin
Given the administrator is logged in and on the Invoice Creation page
When the administrator attempts to create an invoice with amount "-10.00"
Then the system displays validation error "Amount must be greater than zero."
And the invoice is not registered in the database
```

### Scenario 3: Admin dashboard lists all invoices
```gherkin
Given there are two invoices in the system:
| Owner | Amount | Status |
| George Franklin | 50.00 | UNPAID |
| Betty Davis | 120.00 | PAID |
When the administrator views the Admin Invoices dashboard
Then they see both invoices in the invoice list
And the status badge for Betty Davis is "PAID"
```

---

## 4. TDD Test Outlines

We will write unit and slice integration tests using **JUnit 5** and **Mockito**.

### Backend JUnit Test Skeletons

#### 1. Repository Tests (`InvoiceRepositoryTests.java`)
```java
package org.springframework.samples.petclinic.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.model.Invoice;
import org.springframework.samples.petclinic.model.Owner;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class InvoiceRepositoryTests {

    @Autowired
    private InvoiceRepository invoices;

    @Autowired
    private OwnerRepository owners;

    @Test
    void shouldInsertAndFindInvoice() {
        Owner owner = this.owners.findById(1); // Franklin George
        Invoice invoice = new Invoice();
        invoice.setOwner(owner);
        invoice.setAmount(new BigDecimal("75.50"));
        invoice.setIssueDate(LocalDate.now());
        invoice.setDueDate(LocalDate.now().plusDays(30));
        invoice.setDescription("Consultation fee");
        invoice.setPaymentStatus("UNPAID");

        // Save
        this.invoices.save(invoice);

        // Retrieve
        Invoice retrieved = this.invoices.findById(invoice.getId());
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getAmount()).isEqualByComparingTo("75.50");
    }
}
```

#### 2. Controller Validation Tests (`InvoiceControllerTests.java`)
```java
package org.springframework.samples.petclinic.web;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.samples.petclinic.repository.InvoiceRepository;
import org.springframework.samples.petclinic.repository.OwnerRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(InvoiceController.class)
class InvoiceControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceRepository invoices;

    @MockBean
    private OwnerRepository owners;

    @Test
    void shouldRejectNegativeInvoiceAmount() throws Exception {
        mockMvc.perform(post("/admin/invoices/new")
            .param("owner.id", "1")
            .param("amount", "-10.00")
            .param("description", "Consultation")
            .param("issueDate", "2026-07-14")
            .param("dueDate", "2026-08-14")
        )
        .andExpect(status().isOk())
        .andExpect(model().attributeHasErrors("invoice"))
        .andExpect(model().attributeHasFieldErrors("invoice", "amount"))
        .andExpect(view().name("invoices/invoiceForm"));
    }
}
```
