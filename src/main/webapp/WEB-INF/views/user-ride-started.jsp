<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Ride In Progress</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user-ride-started.css">
</head>
<body>
<div class="ride-card">
    <h2>Ride Status: <span id="ride-status">${booking.bookingStatus}</span></h2>

    <p><strong>Pickup:</strong> ${booking.route.pickupLocation}</p>
    <p><strong>Drop:</strong> ${booking.route.dropLocation}</p>
    <p><strong>Fare:</strong> ${booking.fareAmount} INR</p>

    <!-- Driver Info -->
    <div id="driver-card" class="${booking.driver != null ? '' : 'hidden'}">
        <p><strong>Driver:</strong> <span id="driver-name">${booking.driver != null ? booking.driver.displayName : ''}</span></p>
        <p><strong>Mobile:</strong> <span id="driver-mobile">${booking.driver != null ? booking.driver.mobileNumber : ''}</span></p>
    </div>

    <!-- Show only date part using substring -->
    <p><strong>Start Time:</strong> ${fn:substring(booking.startTime, 0, 10)}</p>

    <c:if test="${not empty message}">
        <p class="message">${message}</p>
    </c:if>

    <!-- Hidden values for JS -->
    <input type="hidden" id="bookingId" value="${booking.bookingId}">
    <input type="hidden" id="userId" value="${userId}">
</div>

<script>
    const contextPath = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/user-ride-started.js"></script>
</body>
</html>
