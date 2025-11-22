<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Current Ride</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/current-rides.css">
</head>
<body>

<h1 class="page-title"> Current Ride</h1>

<!-- Flash messages -->
<c:if test="${not empty message}">
    <p class="success-msg">${message}</p>
</c:if>
<c:if test="${not empty errorMessage}">
    <p class="error-msg">${errorMessage}</p>
</c:if>

<c:choose>
    <c:when test="${currentRide != null}">
        <div class="ride-card">
            <p><b>Pickup:</b> ${currentRide.route.pickupLocation}</p>
            <p><b>Drop:</b> ${currentRide.route.dropLocation}</p>
            <p><b>Fare:</b> ₹${currentRide.fareAmount}</p>
            <p><b>User:</b> ${currentRide.user.displayName}</p>
            <p class="ride-status"><b>Status:</b> ${currentRide.bookingStatus}</p>

            <div class="card-actions">
                <!-- Start Ride Form -->
                <c:if test="${currentRide.bookingStatus == 'ACCEPTED'}">
                    <form action="${pageContext.request.contextPath}/driver/${driverId}/start/${currentRide.bookingId}" method="post" style="display:inline;">
                        <button type="submit" class="ride-btn btn-start">
                            <i class="fa-solid fa-play"></i> Start Ride
                        </button>
                    </form>
                </c:if>

                <!-- Complete Ride Form -->
                <c:if test="${currentRide.bookingStatus == 'ONGOING'}">
                    <form action="${pageContext.request.contextPath}/driver/${driverId}/complete/${currentRide.bookingId}" method="post" style="display:inline;">
                        <button type="submit" class="ride-btn btn-complete">
                            <i class="fa-solid fa-flag-checkered"></i> Complete Ride
                        </button>
                    </form>
                </c:if>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <p>No current ride assigned.</p>
    </c:otherwise>
</c:choose>

<form action="${pageContext.request.contextPath}/driver/${driverId}/home" method="get">
    <button type="submit" class="btn-back-home">⬅ Back to Home</button>
</form>

</body>
</html>
