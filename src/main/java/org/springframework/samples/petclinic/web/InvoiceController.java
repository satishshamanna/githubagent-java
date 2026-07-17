/*
 * Version Number: 1.0.0
 * User Story Number: US01
 * Date & Time of Change: 2026-07-14T13:01:40+05:30
 * User Name: Satish
 * Brief Description of Change: Created InvoiceController for admin invoice generation and dashboard mapping.
 */
package org.springframework.samples.petclinic.web;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import jakarta.validation.Valid;
import org.springframework.samples.petclinic.model.Invoice;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
public class InvoiceController {

    private final ClinicService clinicService;

    public InvoiceController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping("/admin/invoices")
    public String listAllInvoices(Map<String, Object> model) {
        model.put("invoices", this.clinicService.findAllInvoices());
        return "invoices/invoiceList";
    }

    @GetMapping("/admin/invoices/new")
    public String initCreationForm(Map<String, Object> model) {
        Invoice invoice = new Invoice();
        invoice.setIssueDate(LocalDate.now());
        invoice.setDueDate(LocalDate.now().plusDays(30));
        model.put("invoice", invoice);
        model.put("owners", this.clinicService.findOwnerByLastName(""));
        return "invoices/invoiceForm";
    }

    @PostMapping("/admin/invoices/new")
    public String processCreationForm(@Valid @ModelAttribute("invoice") Invoice invoice, BindingResult result, Map<String, Object> model) {
        // Validation: amount must be positive
        if (invoice.getAmount() != null && invoice.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            result.rejectValue("amount", "positiveAmount", "Amount must be greater than zero.");
        }

        // Validation: due date must not be before issue date
        if (invoice.getIssueDate() != null && invoice.getDueDate() != null && invoice.getDueDate().isBefore(invoice.getIssueDate())) {
            result.rejectValue("dueDate", "invalidDueDate", "Due date cannot be before issue date.");
        }

        if (result.hasErrors()) {
            model.put("owners", this.clinicService.findOwnerByLastName(""));
            return "invoices/invoiceForm";
        }

        invoice.setPaymentStatus("UNPAID");
        this.clinicService.saveInvoice(invoice);
        return "redirect:/admin/invoices";
    }
}
