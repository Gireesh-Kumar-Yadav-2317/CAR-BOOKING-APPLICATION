function paymentDone() {
    // Hide payment buttons
    document.querySelectorAll('.payment-btn').forEach(btn => btn.style.display = 'none');

    // Show success message and back link
    document.getElementById('paymentMessage').style.display = 'block';
    document.getElementById('backHomeLink').style.display = 'inline-block';

    // Extract userId from backHomeLink URL
    const userLink = document.getElementById('backHomeLink').getAttribute('href');
    const userId = userLink.split('/')[2];

    // Send POST request to server to mark payment success
    fetch(`${window.location.origin}${window.location.pathname.split("/users")[0]}/users/payment-success`, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: "userId=" + encodeURIComponent(userId)
    }).then(res => {
        if (!res.ok) console.error("Payment success request failed");
    }).catch(error => console.error("Payment success error:", error));
}
