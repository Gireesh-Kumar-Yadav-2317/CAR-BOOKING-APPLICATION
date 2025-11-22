<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Pending Rides</title>

    <!-- ✅ External CSS -->
    <link rel="stylesheet" href="<c:url value='/css/pending-rides.css' />"/>

    <!-- ✅ Optional External JS -->
    <script src="<c:url value='/js/pending-rides.js' />" defer></script>
</head>
<body>
<div class="card">
    <h2>Pending Rides for Driver #${driverId}</h2>

    <!-- Flash message for ride actions -->
    <c:if test="${not empty message}">
        <div class="message">${message}</div>
    </c:if>

    <!-- No rides -->
    <c:if test="${empty rides}">
        <div>No pending rides.</div>
    </c:if>

    <!-- Rides table -->
    <c:if test="${not empty rides}">
        <table>
            <thead>
                <tr>
                    <th>Passenger</th>
                    <th>Pickup</th>
                    <th>Drop</th>
                    <th>Fare</th>
                    <th>⚡ Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="ride" items="${rides}">
                    <tr>
                        <td>${ride.user.displayName}</td> <!-- Display passenger name -->
                        <td>${ride.route.pickupLocation}</td>
                        <td>${ride.route.dropLocation}</td>
                        <td>₹${ride.fareAmount}</td>
                        <td>
                            <form style="display:inline;"
                                  action="${pageContext.request.contextPath}/driver/${driverId}/accept/${ride.bookingId}"
                                  method="post">
                                <button type="submit">Accept</button>
                            </form>
                            <form style="display:inline;"
                                  action="${pageContext.request.contextPath}/driver/${driverId}/reject/${ride.bookingId}"
                                  method="post">
                                <button type="submit">Reject</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>

    <a href="${pageContext.request.contextPath}/driver/${driverId}/home">Back to Home</a>
</div>
</body>
</html>
