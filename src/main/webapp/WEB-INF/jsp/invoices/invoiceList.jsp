<%--
 * Version Number: 1.0.0
 * User Story Number: US01
 * Date & Time of Change: 2026-07-14T13:01:40+05:30
 * User Name: Satish
 * Brief Description of Change: Created invoiceList.jsp for billing dashboard view.
--%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="invoices">
    <h2>Billing Invoices</h2>

    <table class="table table-striped">
        <thead>
        <tr>
            <th>Invoice ID</th>
            <th>Owner</th>
            <th>Description</th>
            <th>Amount</th>
            <th>Issue Date</th>
            <th>Due Date</th>
            <th>Status</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="invoice" items="${invoices}">
            <tr>
                <td><c:out value="${invoice.id}"/></td>
                <td>
                    <a href="<spring:url value="/owners/${invoice.owner.id}" htmlEscape="true" />">
                        <c:out value="${invoice.owner.firstName} ${invoice.owner.lastName}"/>
                    </a>
                </td>
                <td><c:out value="${invoice.description}"/></td>
                <td>$<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${invoice.amount}"/></td>
                <td><fmt:parseDate value="${invoice.issueDate}" pattern="yyyy-MM-dd" var="parsedIssueDate" type="date"/>
                    <fmt:formatDate value="${parsedIssueDate}" pattern="yyyy/MM/dd"/></td>
                <td><fmt:parseDate value="${invoice.dueDate}" pattern="yyyy-MM-dd" var="parsedDueDate" type="date"/>
                    <fmt:formatDate value="${parsedDueDate}" pattern="yyyy/MM/dd"/></td>
                <td>
                    <c:choose>
                        <c:when var="isPaid" test="${invoice.paymentStatus eq 'PAID'}">
                            <span class="badge bg-success">PAID</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-warning text-dark">UNPAID</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <br/>
    <a class="btn btn-primary" href="<spring:url value="/admin/invoices/new" htmlEscape="true" />">Create Invoice</a>

</petclinic:layout>
