<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Update Driver</title>
    <link rel="stylesheet" href="<c:url value='/css/driver-update.css' />">
</head>
<body>

<div class="update-container">
    <h2>Update Driver</h2>

    <form action="<c:url value='/admin/driver/${driver.driverId}' />" method="post">
        <input type="hidden" name="_method" value="put">
        <input type="hidden" name="adminId" value="${adminId}">

        <div class="form-item">
            <label for="displayName">Name:</label>
            <input type="text" id="displayName" name="displayName" value="${driver.displayName}" required>
        </div>

        <div class="form-item">
            <label for="mobileNumber">Mobile:</label>
            <input type="text" id="mobileNumber" name="mobileNumber" value="${driver.mobileNumber}" required>
        </div>

        <div class="form-item">
            <label for="licenseNumber">License:</label>
            <input type="text" id="licenseNumber" name="licenseNumber" value="${driver.licenseNumber}" required>
        </div>

        <!-- Cab Type Dropdown -->
        <div class="form-item">
            <label for="cabType">Cab Type:</label>
            <select id="cabType" name="cabType" required>
                <option value="Mini" <c:if test="${driver.cabType eq 'Mini'}">selected</c:if>>Mini</option>
                <option value="Sedan" <c:if test="${driver.cabType eq 'Sedan'}">selected</c:if>>Sedan</option>
                <option value="SUV" <c:if test="${driver.cabType eq 'SUV'}">selected</c:if>>SUV</option>
                <option value="Luxury" <c:if test="${driver.cabType eq 'Luxury'}">selected</c:if>>Luxury</option>
            </select>
        </div>

        <div class="form-item">
            <label for="city">City:</label>
            <select id="city" name="city.cityId" required>
                <c:forEach var="c" items="${cities}">
                    <option value="${c.cityId}" <c:if test="${c.cityId eq driver.city.cityId}">selected</c:if>>${c.cityName}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-item">
            <label for="status">Status:</label>
            <select id="status" name="status" required>
                <c:forEach var="s" items="${driverStatuses}">
                    <option value="${s.name()}" <c:if test="${s eq driver.status}">selected</c:if>>${s.name()}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn-update">Update</button>
            <button type="button" class="btn-back" onclick="window.location.href='<c:url value='/admin/${adminId}/monitor' />'">Back</button>
        </div>
    </form>
</div>

</body>
</html>
