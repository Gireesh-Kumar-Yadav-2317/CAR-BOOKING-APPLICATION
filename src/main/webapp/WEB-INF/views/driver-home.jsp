<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Driver Home</title>

    <!-- Font Awesome for ✔ ✖ icons -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
          crossorigin="anonymous" referrerpolicy="no-referrer"/>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/driver-home.css">
</head>
<body>
    <div class="top-bar">
        <form action="${pageContext.request.contextPath}/common/login" method="get">
            <button type="submit" class="logout-btn">
                <i class="fa-solid fa-right-from-bracket icon"></i> Logout
            </button>
        </form>

        </form>
    </div>


<!-- ========== DRIVER PROFILE CARD ONLY ========== -->
<div class="center-wrapper">
    <div class="profile-card">
        <h2>Driver Details</h2>
        <p><b>Name:</b> ${driver.displayName}</p>
        <p><b>Username:</b> ${driver.username}</p>
        <p><b>Mobile:</b> ${driver.mobileNumber}</p>
        <p><b>City:</b> ${driver.city.cityName}</p>
        <p><b>Status:</b> ${driver.status}</p>
    </div>
</div>

<!-- ===== AUTO POPUP FOR NEW RIDE ===== -->
<div id="autoPopup" class="popup-overlay">
    <div class="popup-box">
        <h2> New Ride Request! </h2>
        <p id="popupDetails">Loading…</p>

        <div class="popup-actions">
            <form id="popupAcceptForm" method="post">
                <button type="submit" class="btn-accept">
                    <i class="fa-solid fa-check"></i> ACCEPT
                </button>
            </form>
            <form id="popupRejectForm" method="post">
                <button type="submit" class="btn-reject">
                    <i class="fa-solid fa-xmark"></i> REJECT
                </button>
            </form>
        </div>
    </div>
</div>

<script>
    const contextPath = '${pageContext.request.contextPath}';
    const driverId    = '${driver.driverId}';
</script>
<script src="${pageContext.request.contextPath}/js/driver-home.js"></script>
</body>
</html>
