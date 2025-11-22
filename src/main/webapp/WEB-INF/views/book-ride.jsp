<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Book Ride</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/book-ride.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <script src="${pageContext.request.contextPath}/js/book-ride.js"></script>
</head>
<body>
    <div class="form-container">
        <h2><i class="fas fa-car"></i> Book a Ride</h2>

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/users/book-ride" method="post">
            <input type="hidden" name="userId" value="${bookingRequest.userId}" />
            <input type="hidden" name="cityId" value="${bookingRequest.cityId}" />

            <div class="field">
                <i class="fas fa-map-marker-alt"></i>
                <select name="pickupLocation" id="pickupSelect" onchange="updateDropLocations()" required>
                    <option value="">--Select Pickup--</option>
                    <c:forEach var="route" items="${routes}">
                        <c:if test="${route.city.cityId == bookingRequest.cityId}">
                            <option value="${route.pickupLocation}"
                                <c:if test="${route.pickupLocation == selectedPickup}">selected</c:if>>
                                ${route.pickupLocation}
                            </option>
                        </c:if>
                    </c:forEach>
                </select>
            </div>

            <div class="field">
                <i class="fas fa-map-pin"></i>
                <select name="dropLocation" id="dropSelect" required>
                    <option value="">--Select Drop--</option>
                </select>
            </div>

            <button type="submit"><i class="fas fa-check-circle"></i> Confirm Book Ride</button>
        </form>
    </div>

    <script>
        const bookingRequest = {
            cityId: ${bookingRequest.cityId},
            selectedPickup: '${selectedPickup}',
            selectedDrop: '${selectedDrop}'
        };

        const routes = [
            <c:forEach var="route" items="${routes}">
                {pickup: '${route.pickupLocation}', drop: '${route.dropLocation}', cityId: ${route.city.cityId}},
            </c:forEach>
        ];
    </script>
</body>
</html>
