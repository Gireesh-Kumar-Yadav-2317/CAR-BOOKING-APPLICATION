<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Searching for a Driver</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/searching-driver.css">
</head>
<body>
<div class="container">

    <!-- Hidden inputs for JS -->
    <input type="hidden" id="bookingId" value="${booking.bookingId}">
    <input type="hidden" id="userId" value="${booking.user.userId}">

    <!-- Status Heading -->
    <h2 id="status-heading">Searching for a Driver...</h2>
    <p id="status-message">Please wait while we find the best driver for your ride.</p>

    <!-- Spinner -->
    <div id="spinner" class="spinner"></div>

    <!-- Driver Details Card -->
    <div id="driver-card" class="driver-card hidden">
        <h3>Driver Details</h3>
        <p><strong>Name:</strong> <span id="driver-name"></span></p>
        <p><strong>Mobile:</strong> <span id="driver-mobile"></span></p>
    </div>

</div>

<!-- Pass Spring context path to JS -->
<script>
    window.APP_CONTEXT = '${pageContext.request.contextPath}';
</script>

<!-- External JS -->
<script src="${pageContext.request.contextPath}/js/searching-driver.js"></script>
</body>
</html>
