function trimInputs() {
    const username = document.querySelector('input[name="username"]');
    const password = document.querySelector('input[name="password"]');
    username.value = username.value.trim();
    password.value = password.value.trim();
}
