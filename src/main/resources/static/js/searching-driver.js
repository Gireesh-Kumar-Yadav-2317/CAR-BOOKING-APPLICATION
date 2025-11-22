document.addEventListener("DOMContentLoaded", function () {
    const contextPath = window.APP_CONTEXT || '';
    const bookingId = document.getElementById("bookingId").value;
    const userId = document.getElementById("userId").value;

    const heading = document.getElementById('status-heading');
    const message = document.getElementById('status-message');
    const spinner = document.getElementById('spinner');
    const driverCard = document.getElementById('driver-card');
    const driverName = document.getElementById('driver-name');
    const driverMobile = document.getElementById('driver-mobile');

    let interval = null;
    let hasRedirected = false;
    let isFetching = false; // prevent overlapping fetches

    const pollStatus = async () => {
        if (hasRedirected || isFetching) return;

        isFetching = true;
        try {
            const res = await fetch(`${contextPath}/users/booking-status?bookingId=${bookingId}`, { cache: 'no-cache' });
            if (!res.ok) return;

            const data = await res.json();
            const status = data.status;

            // Update UI dynamically
            switch (status) {
                case 'PENDING':
                    heading.textContent = 'Searching for a Driver...';
                    message.textContent = 'Please wait while we find the best driver for your ride.';
                    spinner.style.display = 'block';
                    driverCard.classList.add('hidden');
                    break;

                case 'ACCEPTED':
                    heading.textContent = 'Driver Found!';
                    message.textContent = 'Your driver is on the way.';
                    spinner.style.display = 'none';
                    driverCard.classList.remove('hidden');
                    driverName.textContent = data.driverName || 'Not Available';
                    driverMobile.textContent = data.driverMobile || 'Not Available';
                    break;

                case 'ONGOING':
                case 'COMPLETED':

                    hasRedirected = true;
                    clearInterval(interval);
                    window.location.href = `${contextPath}/users/ride-started?bookingId=${bookingId}&userId=${userId}`;
                    break;

                 case 'REJECTED':
                                    hasRedirected = true;
                                    clearInterval(interval);
                                    // Redirect back to searching-driver page to find another driver
                                    window.location.href = `${contextPath}/users/searching-driver?bookingId=${bookingId}`;
                                    break;

                default:
                    console.warn('Unknown status:', status);
            }
        } catch (err) {
            console.error('Error fetching booking status:', err);
        } finally {
            isFetching = false;
        }
    };

    interval = setInterval(pollStatus, 3000);
    pollStatus();

    window.addEventListener('beforeunload', () => clearInterval(interval));
});
