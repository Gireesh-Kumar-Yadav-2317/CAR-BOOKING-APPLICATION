<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Home</title>
    <title>Home</title>

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <!-- Your CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user-home.css"/>

    <!-- Your JS -->
    <script src="${pageContext.request.contextPath}/js/user-home.js" defer></script>

</head>
<body>

    <!-- Logout button (top-right corner) -->
    <div class="logout-container">
        <form action="${pageContext.request.contextPath}/common/login" method="get">
            <button type="submit" class="logout-btn">
                <i class="fas fa-sign-out-alt icon"></i> Logout
            </button>
        </form>
    </div>

    <!-- Main Home Card -->
    <div class="home-container">

        <!-- Display Name -->
        <h2>
            <i class="fas fa-user-circle icon"></i>
            Welcome,
            <c:choose>
                <c:when test="${not empty sessionScope.loggedInUser}">
                    ${sessionScope.loggedInUser.displayName}
                </c:when>
                <c:when test="${not empty user}">
                    ${user.displayName}
                </c:when>
                <c:otherwise>User</c:otherwise>
            </c:choose>
            !
        </h2>

        <!-- Mobile Number -->
        <p>
            <i class="fas fa-mobile-alt icon"></i>
            <strong>Mobile:</strong>
            <c:choose>
                <c:when test="${not empty sessionScope.loggedInUser}">
                    ${sessionScope.loggedInUser.mobileNumber}
                </c:when>
                <c:when test="${not empty user}">
                    ${user.mobileNumber}
                </c:when>
                <c:otherwise>N/A</c:otherwise>
            </c:choose>
        </p>

        <!-- City -->
        <p>
            <i class="fas fa-city icon"></i>
            <strong>City:</strong>
            <c:choose>
                <c:when test="${not empty sessionScope.loggedInUser and not empty sessionScope.loggedInUser.city}">
                    ${sessionScope.loggedInUser.city.cityName}
                </c:when>
                <c:when test="${not empty user and not empty user.city}">
                    ${user.city.cityName}
                </c:when>
                <c:otherwise>N/A</c:otherwise>
            </c:choose>
        </p>

        <!-- Book Ride Button -->
        <c:choose>
            <c:when test="${not empty sessionScope.loggedInUser and not empty sessionScope.loggedInUser.city}">
                <a href="${pageContext.request.contextPath}/users/book-ride?userId=${sessionScope.loggedInUser.userId}&cityId=${sessionScope.loggedInUser.city.cityId}">
                    <button class="book-btn"><i class="fas fa-car-side icon"></i> Book Ride</button>
                </a>
            </c:when>
            <c:when test="${not empty user and not empty user.city}">
                <a href="${pageContext.request.contextPath}/users/book-ride?userId=${user.userId}&cityId=${user.city.cityId}">
                    <button class="book-btn"><i class="fas fa-car-side icon"></i> Book Ride</button>
                </a>
            </c:when>
        </c:choose>

    </div>
</body>
</html>
