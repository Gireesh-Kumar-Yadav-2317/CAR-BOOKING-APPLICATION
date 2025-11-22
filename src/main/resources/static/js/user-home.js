document.addEventListener("DOMContentLoaded", function () {
    const logoutBtn = document.querySelector(".logout-btn");

    if (logoutBtn) {
        logoutBtn.addEventListener("click", function () {
            console.log("Logout clicked"); // For debugging or future logic
        });
    }
});
