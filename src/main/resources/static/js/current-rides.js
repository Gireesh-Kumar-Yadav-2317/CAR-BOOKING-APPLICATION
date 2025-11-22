document.addEventListener('DOMContentLoaded', () => {
    // Select Start and Complete ride buttons
    const startBtn = document.querySelector('.btn-start');
    const completeBtn = document.querySelector('.btn-complete');

    // ===== START RIDE BUTTON =====
    if (startBtn) {
        startBtn.addEventListener('click', () => {
            // Disable button to prevent multiple clicks
            startBtn.disabled = true;
            // Show loading spinner
            startBtn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Starting...';
            // Form will submit normally to Spring Controller
        });
    }

    // ===== COMPLETE RIDE BUTTON =====
    if (completeBtn) {
        completeBtn.addEventListener('click', () => {
            // Disable button to prevent multiple clicks
            completeBtn.disabled = true;
            // Show loading spinner
            completeBtn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Completing...';
            // Form will submit normally to Spring Controller
        });
    }
});
