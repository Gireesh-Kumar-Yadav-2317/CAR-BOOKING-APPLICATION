/**
 * Polls backend every 5 seconds to check for the
 * latest pending ride and shows popup without page refresh.
 */
setInterval(async () => {
    try {
        const res = await fetch(`${contextPath}/driver/${driverId}/pending/latest`);
        if (!res.ok) return;

        const data = await res.json();
        // backend returns: {hasRide:true/false, bookingId, pickup, drop, fare, userName, mobileNumber}
        if (data.hasRide) {
            document.getElementById('popupDetails').innerHTML =
                `<b>Passenger:</b> ${data.userName} <br>
                <b>Mobile Number:</b> ${data.mobileNumber} <br>
                 <b>Pickup:</b> ${data.pickup}<br>
                 <b>Drop:</b> ${data.drop}<br>
                 <b>Fare:</b> ₹${data.fare}`;

            // Update popup form actions
            document.getElementById('popupAcceptForm').action =
                `${contextPath}/driver/${driverId}/accept/${data.bookingId}`;
            document.getElementById('popupRejectForm').action =
                `${contextPath}/driver/${driverId}/reject/${data.bookingId}`;

            // Show popup
            document.getElementById('autoPopup').style.display = 'flex';
        }
    } catch (err) {
        console.error('Polling error', err);
    }
}, 5000); // every 5 seconds
