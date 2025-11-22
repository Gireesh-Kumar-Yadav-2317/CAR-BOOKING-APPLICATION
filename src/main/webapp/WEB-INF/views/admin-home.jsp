<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Home</title>
    <link rel="stylesheet" href="<c:url value='/css/admin-home.css' />">
</head>
<body>
    <!-- Logout button on top-right of the page -->
    <a href="${pageContext.request.contextPath}/common/login" class="logout-btn">Logout</a>

    <div class="page-card">
        <!-- Header -->
        <header>
            <h2>Welcome Admin</h2>
        </header>

        <!-- City Filter -->
        <div class="filter-form">
            <form method="get" action="${pageContext.request.contextPath}/admin/${adminId}/home">
                <label>Select City:</label>
                <select name="cityId">
                    <option value="">All Cities</option>
                    <c:forEach var="city" items="${cities}">
                        <option value="${city.cityId}" <c:if test="${city.cityId eq selectedCityId}">selected</c:if>>
                            ${city.cityName}
                        </option>
                    </c:forEach>
                </select>
                <button type="submit">Apply Filter</button>
            </form>
        </div>

        <!-- Metrics Cards -->
        <div class="container">
            <div class="card">
                <h3>Total Users</h3>
                <p>${totalUsers}</p>
            </div>
            <div class="card">
                <h3>Total Drivers</h3>
                <p>${totalDrivers}</p>
            </div>
            <div class="card">
                <h3>Total Rides</h3>
                <p>${totalRides}</p>
            </div>
        </div>

        <!-- Footer Links -->
        <footer>
            <a href="${pageContext.request.contextPath}/admin/${adminId}/bookings">View Bookings</a>
            <a href="${pageContext.request.contextPath}/admin/${adminId}/monitor">Operations Dashboard</a>
        </footer>
    </div>
</body>
</html>
