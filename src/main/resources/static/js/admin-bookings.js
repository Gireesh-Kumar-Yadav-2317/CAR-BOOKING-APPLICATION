document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('filterForm');

    form.addEventListener('submit', (e) => {
        const city = document.getElementById('cityId').value;
        const status = document.getElementById('status').value;
        const startDate = document.getElementById('startDate').value;
        const endDate = document.getElementById('endDate').value;

        // Prevent form submission if no filter is selected
        if (!city && !status && !startDate && !endDate) {
            e.preventDefault();
            alert('Please select at least one filter to view bookings.');
        }
    });

    const homeBtn = document.querySelector('.btn-home');
    if (homeBtn) {
        homeBtn.addEventListener('click', () => {
            console.log('Home button clicked');
        });
    }
});
