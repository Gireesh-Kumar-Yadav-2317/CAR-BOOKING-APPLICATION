<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Monitoring Dashboard</title>
    <link rel="stylesheet" href="<c:url value='/css/admin-monitor.css' />">
</head>
<body>

<!-- Back to Home Button -->
<button class="btn-home" onclick="window.location.href='<c:url value='/admin/${adminId}/home' />'">
    Back to Home
</button>

<div class="page-container">
    <h2>Admin Monitoring Dashboard</h2>

    <!-- City Filter Card -->
    <div class="filter-box">
        <form id="filterForm" action="<c:url value='/admin/${adminId}/monitor' />" method="get">
            <label for="cityId">City:</label>
            <select id="cityId" name="cityId">
                <option value="">All Cities</option>
                <c:forEach var="city" items="${cities}">
                    <option value="${city.cityId}" <c:if test="${city.cityId eq selectedCityId}">selected</c:if>>
                        ${city.cityName}
                    </option>
                </c:forEach>
            </select>
            <button type="submit" class="btn-filter">Apply Filter</button>
        </form>
    </div>

    <!-- Dashboard Cards -->
    <div class="card-container">
        <div class="card" onclick="showSection('users')"><h3>Users</h3></div>
        <div class="card" onclick="showSection('drivers')"><h3>Drivers</h3></div>
        <div class="card" onclick="showSection('todayRides')"><h3>Today Rides</h3></div>
    </div>

    <!-- Users Table -->
    <div id="users" class="section">
        <h3>Users</h3>
        <div class="table-wrapper">
            <table>
                <tr>
                    <th>ID</th><th>Name</th><th>City</th><th>Mobile</th><th>Status</th><th>Actions</th>
                </tr>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td>${user.userId}</td>
                        <td>${user.displayName}</td>
                        <td>${user.city.cityName}</td>
                        <td>${user.mobileNumber}</td>
                        <td>${user.status}</td>
                        <td>
                            <form action="<c:url value='/admin/user/${user.userId}/update' />" method="get" style="display:inline;">
                                <input type="hidden" name="adminId" value="${adminId}">
                                <button type="submit" class="update-btn">Update</button>
                            </form>
                            <form action="<c:url value='/admin/user/${user.userId}/delete' />" method="post" style="display:inline;">
                                <input type="hidden" name="_method" value="delete"/>
                                <input type="hidden" name="adminId" value="${adminId}">
                                <button type="submit" class="delete-btn" onclick="return confirm('Are you sure?')">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>

    <!-- Drivers Table -->
<div id="drivers" class="section">
    <h3>Drivers</h3>
    <div class="table-wrapper">
        <table>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>City</th>
                <th>Mobile</th>
                <th>License</th>
                <th>Cab Type</th> <!-- Added -->
                <th>Status</th>
                <th>Actions</th>
            </tr>
            <c:forEach var="driver" items="${drivers}">
                <tr>
                    <td>${driver.driverId}</td>
                    <td>${driver.displayName}</td>
                    <td>${driver.city.cityName}</td>
                    <td>${driver.mobileNumber}</td>
                    <td>${driver.licenseNumber}</td>
                    <td>${driver.cabType}</td> <!-- Added -->
                    <td>${driver.status}</td>
                    <td>
                        <form action="<c:url value='/admin/driver/${driver.driverId}/update' />" method="get" style="display:inline;">
                            <input type="hidden" name="adminId" value="${adminId}">
                            <button type="submit" class="update-btn">Update</button>
                        </form>
                        <form action="<c:url value='/admin/driver/${driver.driverId}' />" method="post" style="display:inline;">
                            <input type="hidden" name="_method" value="delete"/>
                            <input type="hidden" name="adminId" value="${adminId}">
                            <button type="submit" class="delete-btn" onclick="return confirm('Are you sure?')">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>

    <!-- Today Rides Table -->
    <div id="todayRides" class="section">
        <h3>Today Rides</h3>
        <div class="table-wrapper">
            <table>
                <tr>
                    <th>ID</th><th>User</th><th>Driver</th><th>City</th><th>Start Time</th><th>End Time</th><th>Status</th><th>Fare</th>
                </tr>
                <c:forEach var="b" items="${todayBookings}">
                    <tr>
                        <td>${b.bookingId}</td>
                        <td>${b.user.displayName}</td>
                        <td>${b.driver.displayName}</td>
                        <td>${b.city.cityName}</td>
                        <td>${b.startTime.toString().substring(0,16)}</td>
                        <td>${b.endTime.toString().substring(0,16)}</td>
                        <td>${b.bookingStatus}</td>
                        <td>${b.fareAmount}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>

</div>

<script>
function showSection(sectionId) {
    document.querySelectorAll('.section').forEach(sec => sec.style.display = 'none');
    document.getElementById(sectionId).style.display = 'block';
}
showSection('users'); // default
</script>

</body>
</html>
