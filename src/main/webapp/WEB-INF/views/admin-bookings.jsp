]<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <!DOCTYPE html>
 <html lang="en">
 <head>
     <meta charset="UTF-8">
     <title>Bookings</title>

     <!-- CSS -->
     <link rel="stylesheet" href="<c:url value='/css/admin-bookings.css' />">

     <!-- JS -->
     <script src="<c:url value='/js/admin-bookings.js' />" defer></script>

     <!-- Prevent caching -->
     <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate"/>
     <meta http-equiv="Pragma" content="no-cache"/>
     <meta http-equiv="Expires" content="0"/>
 </head>
 <body>

 <!-- Home Button -->
 <button type="button"
         class="btn-home"
         onclick="window.location.href='<c:url value='/admin/${adminId}/home' />'">
     Home
 </button>

 <h2>Bookings</h2>

 <!-- Filter Box -->
 <div class="filter-box">
     <form id="filterForm" action="<c:url value='/admin/${adminId}/bookings'/>" method="get">

         <!-- City -->
         <div class="filter-item">
             <label for="cityId">City:</label>
             <select id="cityId" name="cityId">
                 <option value="">Select City</option>
                 <c:forEach var="city" items="${cities}">
                     <option value="${city.cityId}" <c:if test="${city.cityId eq selectedCityId}">selected</c:if>>
                         ${city.cityName}
                     </option>
                 </c:forEach>
             </select>
         </div>

         <!-- Status -->
         <div class="filter-item">
             <label for="status">Status:</label>
             <select id="status" name="status">
                 <option value="">All Status</option>
                 <c:forEach var="s" items="${bookingStatuses}">
                     <option value="${s}" <c:if test="${s eq selectedStatus}">selected</c:if>>
                         ${s}
                     </option>
                 </c:forEach>
             </select>
         </div>

         <!-- Start Date -->
         <div class="filter-item">
             <label for="startDate">Start Date:</label>
             <input type="date" id="startDate" name="startDate" value="${startDate}" />
         </div>

         <!-- End Date -->
         <div class="filter-item">
             <label for="endDate">End Date:</label>
             <input type="date" id="endDate" name="endDate" value="${endDate}" />
         </div>

         <!-- Filter Button -->
         <div class="filter-button-container">
             <button type="submit" class="btn-filter">Filter</button>
         </div>

     </form>
 </div>

 <!-- Bookings Table -->
 <c:if test="${filterApplied}">
     <c:choose>
         <c:when test="${not empty bookings}">
             <table class="bookings-table">
                 <thead>
                     <tr>
                         <th>ID</th>
                         <th>User</th>
                         <th>Driver</th>
                         <th>City</th>
                         <th>Start Time</th>
                         <th>End Time</th>
                         <th>Status</th>
                         <th>Fare</th>
                     </tr>
                 </thead>
                 <tbody>
                     <c:forEach var="booking" items="${bookings}">
                         <tr>
                             <td>${booking.bookingId}</td>
                             <td>${booking.user.displayName}</td>
                             <td>${booking.driver.displayName}</td>
                             <td>${booking.city.cityName}</td>
                             <!-- Display only date without milliseconds -->
                             <td>${booking.startTime.toString().substring(0,16)}</td>
                             <td>${booking.endTime.toString().substring(0,16)}</td>
                             <td>${booking.bookingStatus}</td>
                             <td>${booking.fareAmount}</td>
                         </tr>
                     </c:forEach>
                 </tbody>
             </table>
         </c:when>
         <c:otherwise>
             <p class="no-data">No bookings available for selected filters.</p>
         </c:otherwise>
     </c:choose>
 </c:if>

 <c:if test="${!filterApplied}">
     <p class="no-data">Please select at least one filter to view bookings.</p>
 </c:if>

 </body>
 </html>
