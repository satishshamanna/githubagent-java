# US02: View & Pay Invoices (Owner Flow)

* **Story Point Estimate**: 5 (Fibonacci)
* **Parent Feature**: [F03 - Clinic Billing & Invoicing](file:///d:/SatishAIProjects/12-GitHub_NonSalesforce/docs/features/F03/01_feature_design.md)

---

## 1. Description
**As a** Pet Owner  
**I want to** view my invoices and pay outstanding balances online  
**So that** I can settle my bills conveniently and keep a history of my payments.

---

## 2. Acceptance Criteria

| ID | Given | When | Then |
|---|---|---|---|
| AC-2.1 | Owner is on the Owner Details page | They look under their general details | They see a link or section titled "View Invoices". |
| AC-2.2 | Owner clicks "View Invoices" | They are redirected to the Invoice List page | They see all invoices issued to their account, with columns for Issue Date, Amount, Status, and Action. |
| AC-2.3 | Owner clicks on an unpaid invoice | They view the Invoice Details page | They see the billing details and a "Pay Now" button. |
| AC-2.4 | Owner is on the simulated checkout page | They submit valid mock credit card details | The invoice status changes to `PAID`, `paymentDate` is set to the current date, and they see a payment success screen. |
| AC-2.5 | Owner attempts to pay an invoice that is already paid | They submit the request | The system blocks the request, displays error message "This invoice has already been paid.", and prevents double charging. |

---

## 3. BDD Gherkin Scenarios

### Scenario 1: Successfully pay outstanding invoice online
```gherkin
Given the owner "George Franklin" has an outstanding invoice of "75.00" in status "UNPAID"
When the owner views their Invoice Details page
And clicks "Pay Now"
And enters credit card number "4111222233334444", expiration "12/28", CVV "123"
And submits the payment
Then the invoice status updates to "PAID"
And the payment date is set to today's date
And the owner sees a receipt showing payment success
```

### Scenario 2: Prevent double payment of an invoice
```gherkin
Given the owner "Betty Davis" has an invoice in status "PAID"
When the owner submits a POST request to "/owners/2/invoices/5/pay"
Then the system returns a validation error "This invoice has already been paid."
And no new payment transactions or duplicate statuses are registered
```

---

## 4. TDD Test Outlines

We will write unit and slice integration tests using **JUnit 5** and **Mockito**.

### Backend JUnit Test Skeletons

#### 1. Repository Tests (`InvoiceRepositoryTests.java`)
```java
package org.springframework.samples.petclinic.repository;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.model.Invoice;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class InvoiceRepositoryOwnerTests {

    @Autowired
    private InvoiceRepository invoices;

    @Test
    void shouldFindInvoicesByOwnerId() {
        // Owner with ID 1 Franklin George has 1 sample invoice
        List<Invoice> results = this.invoices.findByOwnerId(1);
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getOwner().getId()).isEqualTo(1);
    }
}
```

#### 2. Controller Payment Flow & Double Payment Prevention Tests (`InvoiceControllerTests.java`)
```java
package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.samples.petclinic.model.Invoice;
import org.springframework.samples.petclinic.repository.InvoiceRepository;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(InvoiceController.class)
class InvoiceControllerPaymentTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceRepository invoices;

    @Test
    void shouldPreventPayingAlreadyPaidInvoice() throws Exception {
        Invoice paidInvoice = new Invoice();
        paidInvoice.setId(10);
        paidInvoice.setPaymentStatus("PAID");
        
        given(this.invoices.findById(10)).willReturn(paidInvoice);

        mockMvc.perform(post("/owners/1/invoices/10/pay")
            .param("cardNumber", "4111222233334444")
            .param("expiration", "12/28")
            .param("cvv", "123")
        )
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("errorMessage"))
        .andExpect(model().attribute("errorMessage", "This invoice has already been paid."))
        .andExpect(view().name("invoices/paymentForm"));
    }
}
```
