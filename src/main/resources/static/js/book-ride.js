function updateDropLocations() {
    const pickupSelect = document.getElementById("pickupSelect");
    const dropSelect = document.getElementById("dropSelect");
    const selectedPickup = pickupSelect.value;

    dropSelect.innerHTML = '<option value="">--Select Drop--</option>';

    routes.forEach(function(r) {
        if (r.cityId === bookingRequest.cityId && r.pickup === selectedPickup && r.drop !== selectedPickup) {
            const opt = document.createElement("option");
            opt.value = r.drop;
            opt.text = r.drop;
            if (r.drop === bookingRequest.selectedDrop) opt.selected = true;
            dropSelect.add(opt);
        }
    });
}

// Initialize on page load
window.onload = function() {
    const pickupSelect = document.getElementById("pickupSelect");
    pickupSelect.value = bookingRequest.selectedPickup || '';
    updateDropLocations();
};
