<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="<c:url value='/css/login.css' />"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <script src="<c:url value='/js/login.js' />"></script>

</head>
<body>
<div class="form-container">
    <h2><i class="fas fa-lock"></i> Login</h2>

    <c:if test="${not empty error}">
        <div class="message error">${error}</div>
    </c:if>
    <c:if test="${not empty success}">
        <div class="message success">${success}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/common/login" method="post" onsubmit="trimInputs()">
        <div class="input-group">
            <input type="text" name="username" placeholder="Enter Username" required/>
            <i class="fas fa-user"></i>
        </div>
        <div class="input-group">
            <input type="password" name="password" placeholder="Enter Password" required/>
            <i class="fas fa-key"></i>
        </div>
        <input type="submit" value="Login"/>
    </form>




    <p>Don't have an account? <a href="${pageContext.request.contextPath}/common/signup">Signup</a></p>
</div>
</body>
</html>
