document.addEventListener('DOMContentLoaded', () => {
    const paymentBtn = document.getElementById('paymentReceived');
    const confirmationMsg = document.getElementById('confirmationMsg');

    paymentBtn.addEventListener('click', () => {
        confirmationMsg.textContent = "✅ Payment has been received successfully!";
        paymentBtn.disabled = true;                 // Disable button
        paymentBtn.style.opacity = '0.6';           // Fade button
    });
});
