<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Signup</title>
    <link rel="stylesheet" href="<c:url value='/css/signup.css' />"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <script src="<c:url value='/js/signup.js' />"></script>


</head>
<body>
<div class="signup-form">
    <h2><i class="fas fa-user-plus"></i> Signup</h2>

    <c:if test="${not empty error}"><div class="message error">${error}</div></c:if>
    <c:if test="${not empty success}"><div class="message success">${success}</div></c:if>

    <form action="${pageContext.request.contextPath}/common/signup" method="post">

        <!-- Role selection -->
        <div class="role-section">
            <input type="radio" name="role" value="USER" onclick="toggleDriverFields()" checked/> User
            <input type="radio" name="role" value="DRIVER" onclick="toggleDriverFields()"/> Driver
            <input type="radio" name="role" value="ADMIN" onclick="toggleDriverFields()"/> Admin
        </div>

        <!-- Common Fields -->
        <div class="input-group"><i class="fas fa-envelope"></i>
            <input type="email" name="username" placeholder="Email" required/>
        </div>
        <div class="input-group"><i class="fas fa-lock"></i>
            <input type="password" name="password" placeholder="Password" required/>
        </div>
        <div class="input-group"><i class="fas fa-user"></i>
            <input type="text" name="displayName" placeholder="Display Name" required/>
        </div>
        <div class="input-group"><i class="fas fa-phone"></i>
            <input type="text" name="mobileNumber" placeholder="Mobile Number" required/>
        </div>
        <div class="input-group"><i class="fas fa-city"></i>
            <select name="city.cityId" required>
                <option value="" disabled selected>Select City</option>
                <c:forEach var="city" items="${cities}">
                    <option value="${city.cityId}">${city.cityName}</option>
                </c:forEach>
            </select>

        </div>

        <!-- Driver-only fields -->
        <div id="driverFields" class="driver-fields">
            <div class="input-group"><i class="fas fa-taxi"></i>
                <input type="text" name="cabNumber" placeholder="Cab Number"/>
            </div>
            <div class="input-group"><i class="fas fa-id-card"></i>
                <input type="text" name="licenseNumber" placeholder="License Number"/>
            </div>
            <div class="input-group"><i class="fas fa-car"></i>
                <select id="cabType" name="cabType">
                    <option value="" disabled hidden>Select Cab Type</option>
                    <option value="SEDAN">Sedan</option>
                    <option value="SUV">SUV</option>
                    <option value="HATCHBACK">Hatchback</option>
                    <option value="MINIVAN">Minivan</option>
                </select>
            </div>
            <div class="input-group"><i class="fas fa-toggle-on"></i>
                <select id="status" name="status">
                    <option value="" disabled hidden>Select Status</option>
                    <option value="ACTIVE">Active</option>
                    <option value="INACTIVE">Inactive</option>
                </select>
            </div>
        </div>

        <input type="submit" value="Signup"/>
    </form>

    <div class="signin-link">
        Already have an account? <a href="${pageContext.request.contextPath}/common/login">Sign in</a>
    </div>
</div>
</body>
</html>
