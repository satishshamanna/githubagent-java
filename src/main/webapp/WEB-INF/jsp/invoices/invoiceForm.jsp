<%--
 * Version Number: 1.0.0
 * User Story Number: US01
 * Date & Time of Change: 2026-07-14T13:01:40+05:30
 * User Name: Satish
 * Brief Description of Change: Created invoiceForm.jsp for receptionist invoice creation form.
--%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="invoices">
    <jsp:attribute name="customScript">
        <link rel="stylesheet" href="/webjars/flatpickr/4.6.13/dist/flatpickr.min.css">
        <script src="/webjars/flatpickr/4.6.13/dist/flatpickr.js"></script>
        <script>
            flatpickr("#issueDate", {
                dateFormat: "Y-m-d"
            });
            flatpickr("#dueDate", {
                dateFormat: "Y-m-d"
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <h2>Create Invoice</h2>
        <form:form modelAttribute="invoice" class="form-horizontal">
            <input type="hidden" name="id" value="${invoice.id}"/>

            <div class="form-group row">
                <label class="col-sm-2 col-form-label">Owner</label>
                <div class="col-sm-10">
                    <form:select class="form-select" path="owner.id">
                        <option value="">-- Select Owner --</option>
                        <c:forEach var="o" items="${owners}">
                            <option value="${o.id}" ${o.id == invoice.owner.id ? 'selected' : ''}>
                                <c:out value="${o.firstName} ${o.lastName}"/>
                            </option>
                        </c:forEach>
                    </form:select>
                    <form:errors path="owner" cssClass="text-danger help-inline"/>
                </div>
            </div>

            <petclinic:inputField label="Amount ($)" name="amount"/>
            <petclinic:inputField label="Issue Date" name="issueDate"/>
            <petclinic:inputField label="Due Date" name="dueDate"/>
            <petclinic:inputField label="Description" name="description"/>

            <div class="form-group row">
                <div class="col-sm-offset-2 col-sm-10">
                    <button class="btn btn-primary" type="submit">Create Invoice</button>
                    <a class="btn btn-secondary" href="<spring:url value="/admin/invoices" htmlEscape="true" />">Cancel</a>
                </div>
            </div>
        </form:form>
    </jsp:body>
</petclinic:layout>
