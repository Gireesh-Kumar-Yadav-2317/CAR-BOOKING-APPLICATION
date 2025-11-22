<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Ride Completed</title>

    <!-- Font Awesome -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
          crossorigin="anonymous" referrerpolicy="no-referrer"/>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/ride-completed.css">
</head>
<body>

<h1 class="title">Ride Completed</h1>

<div class="complete-card">
    <p><b>Pickup:</b> ${ride.route.pickupLocation}</p>
    <p><b>Drop:</b> ${ride.route.dropLocation}</p>
    <p><b>Fare:</b> ₹${ride.fareAmount}</p>
    <p><b>User:</b> ${ride.user.displayName} </p>
    <p><b>Mobile Number:</b> ${ride.user.mobileNumber} </p>

    <h3>Show QR for Payment</h3>
    <img id="qrCode" src="data:image/png;base64,${qrCode}" alt="Payment QR Code" class="qr-code"/>

    <div class="action-buttons">
        <button id="paymentReceived" class="btn-action">
            <i class="fa-solid fa-check-circle"></i> Payment Received
        </button>
    </div>

    <p id="confirmationMsg" class="confirmation-msg"></p>

    <a href="${pageContext.request.contextPath}/driver/${ride.driver.driverId}/home" class="home-link">
        ⬅ Back to Home
    </a>
</div>

<script src="${pageContext.request.contextPath}/js/ride-completed.js"></script>
</body>
</html>
