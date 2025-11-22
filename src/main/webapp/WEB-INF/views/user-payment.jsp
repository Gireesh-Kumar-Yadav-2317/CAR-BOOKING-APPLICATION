<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Pay for Your Ride</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user-payment.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
</head>
<body>
<div class="container">
    <h2>Pay for Your Ride</h2>

    <p><strong>Pickup:</strong> ${booking.route.pickupLocation}</p>
    <p><strong>Drop:</strong> ${booking.route.dropLocation}</p>
    <p><strong>Fare:</strong> ${booking.fareAmount} INR</p>
    <p><strong>Driver:</strong> ${booking.driver.displayName}</p>

    <!-- Payment buttons -->
    <button class="payment-btn" onclick="paymentDone()"> <i class="fa-brands fa-google-pay"></i> Pay with GPay </button>
    <button class="payment-btn" onclick="paymentDone()"> <i class="fa-solid fa-wallet"></i> Pay with PhonePe </button>

    <!-- Success message (hidden by default) -->
    <p id="paymentMessage" style="display:none;">Payment Completed Successfully!</p>

    <!-- Back to Home (hidden by default) -->
    <a id="backHomeLink" style="display:none;" href="<c:url value='/users/${booking.user.userId}/home' />">Back to Home</a>
</div>

<script src="${pageContext.request.contextPath}/js/user-payment.js"></script>
</body>
</html>
