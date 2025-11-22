function toggleDriverFields() {
    const role = document.querySelector('input[name="role"]:checked').value;
    const driverSection = document.getElementById('driverFields');
    const cabType = document.getElementById('cabType');
    const status = document.getElementById('status');

    if (role === 'DRIVER') {
        driverSection.classList.add('show');
        cabType.setAttribute('required', 'true');
        status.setAttribute('required', 'true');
    } else {
        driverSection.classList.remove('show');
        cabType.removeAttribute('required');
        status.removeAttribute('required');
    }
}

// Ensure the driver fields are hidden on page load
window.addEventListener('DOMContentLoaded', () => {
    toggleDriverFields(); // checks current role and sets driver section visibility
});
