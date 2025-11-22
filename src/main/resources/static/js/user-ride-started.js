document.addEventListener("DOMContentLoaded", function () {
    const contextPath = window.APP_CONTEXT || '';
    const bookingId = document.getElementById("bookingId").value;
    const userId = document.getElementById("userId").value;
    const rideStatus = document.getElementById("ride-status");

    const driverCard = document.getElementById("driver-card");
    const driverName = document.getElementById("driver-name");
    const driverMobile = document.getElementById("driver-mobile");

    let lastStatus = null; // track last status to prevent DOM flicker
    let hasRedirected = false;
    let interval = null;
    let isFetching = false; // prevent overlapping fetches

    const pollStatus = async () => {
        if (hasRedirected || isFetching) return;
        isFetching = true;

        try {
            const res = await fetch(`${contextPath}/users/booking-status?bookingId=${bookingId}`, { cache: 'no-cache' });
            if (!res.ok) return;

            const data = await res.json();
            const status = data.status;

            // Update status only if it changed
            if (status !== lastStatus) {
                rideStatus.textContent = status;
                lastStatus = status;
            }

            // Update driver info if available
            if (data.driverName && data.driverMobile) {
                if (driverName.textContent !== data.driverName) driverName.textContent = data.driverName;
                if (driverMobile.textContent !== data.driverMobile) driverMobile.textContent = data.driverMobile;
                driverCard.classList.remove("hidden");
            } else {
                driverCard.classList.add("hidden");
            }

            // Handle redirects based on ride status
            if (["ACCEPTED", "ONGOING", "COMPLETED"].includes(status)) {
                hasRedirected = true;
                clearInterval(interval);
                window.location.href = `${contextPath}/users/ride-started?userId=${userId}&bookingId=${bookingId}`;
            } else if (status === "REJECTED") {
                hasRedirected = true;
                clearInterval(interval);
                window.location.href = `${contextPath}/users/searching-driver?bookingId=${bookingId}`;
            }

        } catch (err) {
            console.error("Error fetching ride status:", err);
        } finally {
            isFetching = false;
        }
    };

    // Poll every 5 seconds
    interval = setInterval(pollStatus, 5000);
    pollStatus(); // initial check

    // Cleanup on page unload
    window.addEventListener('beforeunload', () => clearInterval(interval));
});




/*
document.addEventListener("DOMContentLoaded", function () {
    const contextPath = window.APP_CONTEXT || '';
    const bookingId = document.getElementById("bookingId").value;
    const userId = document.getElementById("userId").value;
    const rideStatus = document.getElementById("ride-status");

    const driverCard = document.getElementById("driver-card");
    const driverName = document.getElementById("driver-name");
    const driverMobile = document.getElementById("driver-mobile");

    let hasRedirected = false;

    const pollStatus = async () => {
        if (hasRedirected) return; // Stop polling after redirect

        try {
            const res = await fetch(`${contextPath}/users/booking-status?bookingId=${bookingId}`, { cache: 'no-cache' });
            if (!res.ok) return;

            const data = await res.json();
            const status = data.status;

            rideStatus.textContent = status;

            if (data.driverName && data.driverMobile) {
                driverName.textContent = data.driverName;
                driverMobile.textContent = data.driverMobile;
                driverCard.classList.remove("hidden");
            }

            // Redirect only once
            if (["ACCEPTED", "ONGOING", "COMPLETED"].includes(status)) {
                hasRedirected = true;       // ✅ mark as redirected
                clearInterval(interval);     // ✅ stop polling immediately
                window.location.href = `${contextPath}/users/ride-started?userId=${userId}&bookingId=${bookingId}`;
            }

        } catch (err) {
            console.error("Error fetching ride status:", err);
        }
    };

    const interval = setInterval(pollStatus, 5000); // poll every 5 seconds
    pollStatus(); // initial check

    // Cleanup if page unloads
    window.addEventListener('beforeunload', () => clearInterval(interval));
});
*/
