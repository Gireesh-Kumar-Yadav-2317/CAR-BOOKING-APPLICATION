<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Update User</title>
    <link rel="stylesheet" href="<c:url value='/css/user-update.css' />">
</head>
<body>

<div class="update-container">
    <h2>Update User</h2>

    <!-- Update Form -->
    <form action="<c:url value='/admin/user/${user.userId}' />" method="post">
        <input type="hidden" name="_method" value="put"/>
        <input type="hidden" name="adminId" value="${adminId}">

        <!-- Name -->
        <div class="form-item">
            <label for="displayName">Name:</label>
            <input type="text" id="displayName" name="displayName"
                   value="${user.displayName}" required>
        </div>

        <!-- Mobile -->
        <div class="form-item">
            <label for="mobileNumber">Mobile:</label>
            <input type="text" id="mobileNumber" name="mobileNumber"
                   value="${user.mobileNumber}" required>
        </div>

        <!-- City -->
        <div class="form-item">
            <label for="city">City:</label>
            <select id="city" name="city.cityId" required>
                <c:forEach var="c" items="${cities}">
                    <option value="${c.cityId}" <c:if test="${c.cityId eq user.city.cityId}">selected</c:if>>
                        ${c.cityName}
                    </option>
                </c:forEach>
            </select>
        </div>

        <!-- Status (Enum) -->
        <div class="form-item">
            <label for="status">Status:</label>
            <select id="status" name="status" required>
                <c:forEach var="s" items="${userStatuses}">
                    <option value="${s.name()}" <c:if test="${s eq user.status}">selected</c:if>>
                        ${s.name()}
                    </option>
                </c:forEach>
            </select>
        </div>

        <!-- Action Buttons -->
        <div class="form-actions">
            <button type="submit" class="btn-update">Update</button>
            <button type="button" class="btn-back"
                    onclick="window.location.href='<c:url value='/admin/${adminId}/monitor' />'">
                Back
            </button>
        </div>
    </form>
</div>

</body>
</html>
