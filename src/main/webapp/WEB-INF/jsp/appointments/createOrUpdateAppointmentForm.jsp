<%@ page session="false" trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
            <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
                <%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
                    <%-- Version Number: 1.0.0 User Story Number: US01 Date & Time of Change: 2026-07-13T07:57:32+05:30
                        User Name: Satish Brief Description of Change: Created
                        createOrUpdateAppointmentForm JSP for vet appointment booking. --%>

                        <petclinic:layout pageName="owners">
                            <jsp:attribute name="customScript">
                                <link rel="stylesheet" href="/webjars/flatpickr/4.6.13/dist/flatpickr.min.css">
                                <script src="/webjars/flatpickr/4.6.13/dist/flatpickr.js"></script>
                                <script>
                                    // Store all vets in a JavaScript array
                                    const allVets = [
                                        <c:forEach var="v" items="${vets}" varStatus="status">
                                            {
                                                id: "${v.id}",
                                            name: "<c:out value="${v.firstName} ${v.lastName}" />",
                                            specialties: [
                                            <c:forEach var="spec" items="${v.specialties}" varStatus="specStatus">
                                                "${spec.name}"${!specStatus.last ? ',' : ''}
                                            </c:forEach>
                                            ]
                    }${!status.last ? ',' : ''}
                                        </c:forEach>
                                    ];

                                    const allTimeSlots = ["09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00"];

                                    function filterVets() {
                                        const specialty = document.getElementById('specialty').value;
                                        const vetSelect = document.getElementById('vet');
                                        const currentSelectedVal = vetSelect.value;

                                        // Clear existing options except placeholder
                                        vetSelect.innerHTML = '<option value="">-- Select Vet --</option>';

                                        allVets.forEach(v => {
                                            if (!specialty || v.specialties.includes(specialty)) {
                                                const opt = document.createElement('option');
                                                opt.value = v.id;
                                                opt.textContent = v.name;
                                                if (v.id === currentSelectedVal) {
                                                    opt.selected = true;
                                                }
                                                vetSelect.appendChild(opt);
                                            }
                                        });

                                        // Update time slots as the vet changed
                                        updateTimeSlots();
                                    }

                                    async function updateTimeSlots() {
                                        const vetId = document.getElementById('vet').value;
                                        const date = document.getElementById('date').value;
                                        const timeSlotSelect = document.getElementById('timeSlot');
                                        const currentSelectedVal = timeSlotSelect.value;

                                        // Reset options
                                        timeSlotSelect.innerHTML = '<option value="">-- Select Time Slot --</option>';

                                        allTimeSlots.forEach(slot => {
                                            const opt = document.createElement('option');
                                            opt.value = slot;
                                            opt.textContent = slot;
                                            if (slot === currentSelectedVal) {
                                                opt.selected = true;
                                            }
                                            timeSlotSelect.appendChild(opt);
                                        });

                                        if (!vetId || !date) {
                                            return;
                                        }

                                        try {
                                            const response = await fetch('/appointments/booked-slots?vetId=' + vetId + '&date=' + date);
                                            if (response.ok) {
                                                const bookedSlots = await response.json();
                                                const options = timeSlotSelect.querySelectorAll('option');
                                                options.forEach(opt => {
                                                    if (bookedSlots.includes(opt.value)) {
                                                        opt.disabled = true;
                                                        opt.textContent = opt.value + ' (Booked)';
                                                        if (opt.selected) {
                                                            opt.selected = false;
                                                            timeSlotSelect.value = "";
                                                        }
                                                    }
                                                });
                                            }
                                        } catch (err) {
                                            console.error("Error fetching booked slots:", err);
                                        }
                                    }

                                    document.addEventListener('DOMContentLoaded', () => {
                                        flatpickr("#date", {
                                            dateFormat: "Y-m-d",
                                            onChange: function (selectedDates, dateStr, instance) {
                                                updateTimeSlots();
                                            }
                                        });

                                        document.getElementById('specialty').addEventListener('change', filterVets);
                                        document.getElementById('vet').addEventListener('change', updateTimeSlots);

                                        // Trigger initial filter
                                        filterVets();
                                    });
                                </script>
                            </jsp:attribute>
                            <jsp:body>
                                <h2>Book Appointment</h2>

                                <span id="pet"><strong>Pet</strong></span>
                                <table class="table table-striped" aria-describedby="pet">
                                    <thead>
                                        <tr>
                                            <th scope="col">Name</th>
                                            <th scope="col">Birth Date</th>
                                            <th scope="col">Type</th>
                                            <th scope="col">Owner</th>
                                        </tr>
                                    </thead>
                                    <tr>
                                        <td>
                                            <c:out value="${appointment.pet.name}" />
                                        </td>
                                        <td>
                                            <petclinic:localDate date="${appointment.pet.birthDate}"
                                                pattern="yyyy/MM/dd" />
                                        </td>
                                        <td>
                                            <c:out value="${appointment.pet.type.name}" />
                                        </td>
                                        <td>
                                            <c:out
                                                value="${appointment.pet.owner.firstName} ${appointment.pet.owner.lastName}" />
                                        </td>
                                    </tr>
                                </table>

                                <form:form modelAttribute="appointment" class="form-horizontal">
                                    <spring:hasBindErrors name="appointment">
                                        <c:forEach var="err" items="${errors.globalErrors}">
                                            <div class="alert alert-danger">
                                                <c:out value="${err.defaultMessage}" />
                                            </div>
                                        </c:forEach>
                                    </spring:hasBindErrors>

                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">Specialty</label>
                                        <div class="col-sm-10">
                                            <select id="specialty" class="form-control">
                                                <option value="">-- Select Specialty --</option>
                                                <c:forEach var="spec" items="${specialties}">
                                                    <option value="${spec}">
                                                        <c:out value="${spec}" />
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">Veterinarian</label>
                                        <div class="col-sm-10">
                                            <form:select id="vet" path="vet" class="form-control">
                                                <option value="">-- Select Vet --</option>
                                            </form:select>
                                            <form:errors path="vet" cssClass="help-inline" />
                                        </div>
                                    </div>

                                    <petclinic:inputField label="Date" name="date" />

                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">Time Slot</label>
                                        <div class="col-sm-10">
                                            <form:select id="timeSlot" path="timeSlot" class="form-control">
                                                <option value="">-- Select Time Slot --</option>
                                            </form:select>
                                            <form:errors path="timeSlot" cssClass="help-inline" />
                                        </div>
                                    </div>

                                    <petclinic:inputField label="Description" name="description" />

                                    <div class="form-group">
                                        <div class="col-sm-offset-2 col-sm-10">
                                            <button class="btn btn-primary" type="submit">Submit</button>
                                        </div>
                                    </div>
                                </form:form>
                            </jsp:body>
                        </petclinic:layout>

