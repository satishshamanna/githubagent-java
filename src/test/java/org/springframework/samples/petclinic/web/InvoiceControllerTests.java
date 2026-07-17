/*
 * Version Number: 1.0.0
 * User Story Number: US01
 * Date & Time of Change: 2026-07-14T13:13:13+05:30
 * User Name: Satish
 * Brief Description of Change: Created InvoiceControllerTests verifying invoice dashboard listing, validation, and creation.
 */
package org.springframework.samples.petclinic.web;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Invoice;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig(locations = {"classpath:spring/mvc-test-config.xml", "classpath:spring/mvc-core-config.xml"})
class InvoiceControllerTests {

    private static final int TEST_OWNER_ID = 1;

    @Autowired
    private InvoiceController invoiceController;

    @Autowired
    private ClinicService clinicService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(invoiceController).build();

        Owner owner = new Owner();
        owner.setId(TEST_OWNER_ID);
        owner.setFirstName("George");
        owner.setLastName("Franklin");
        given(this.clinicService.findOwnerById(TEST_OWNER_ID)).willReturn(owner);
        given(this.clinicService.findOwnerByLastName("")).willReturn(Collections.singletonList(owner));
    }

    @Test
    void testListAllInvoices() throws Exception {
        mockMvc.perform(get("/admin/invoices"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("invoices"))
            .andExpect(view().name("invoices/invoiceList"));
    }

    @Test
    void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/admin/invoices/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("invoice"))
            .andExpect(model().attributeExists("owners"))
            .andExpect(view().name("invoices/invoiceForm"));
    }

    @Test
    void testProcessCreationFormSuccess() throws Exception {
        mockMvc.perform(post("/admin/invoices/new")
            .param("owner.id", String.valueOf(TEST_OWNER_ID))
            .param("amount", "100.00")
            .param("description", "Vaccine and consult")
            .param("issueDate", LocalDate.now().toString())
            .param("dueDate", LocalDate.now().plusDays(30).toString())
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/admin/invoices"));
    }

    @Test
    void testProcessCreationFormNegativeAmount() throws Exception {
        mockMvc.perform(post("/admin/invoices/new")
            .param("owner.id", String.valueOf(TEST_OWNER_ID))
            .param("amount", "-10.00") // Negative amount
            .param("description", "Vaccine and consult")
            .param("issueDate", LocalDate.now().toString())
            .param("dueDate", LocalDate.now().plusDays(30).toString())
        )
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("invoice", "amount"))
            .andExpect(view().name("invoices/invoiceForm"));
    }

    @Test
    void testProcessCreationFormDueDateBeforeIssueDate() throws Exception {
        mockMvc.perform(post("/admin/invoices/new")
            .param("owner.id", String.valueOf(TEST_OWNER_ID))
            .param("amount", "50.00")
            .param("description", "Vaccine")
            .param("issueDate", LocalDate.now().toString())
            .param("dueDate", LocalDate.now().minusDays(1).toString()) // Due date before issue date
        )
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("invoice", "dueDate"))
            .andExpect(view().name("invoices/invoiceForm"));
    }
}
