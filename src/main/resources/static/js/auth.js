const BASE_URL = "http://localhost:8080";

// Register
async function register() {

    const user = {
        username: document.getElementById("username").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
        phone: document.getElementById("phone").value
    };

    const response = await fetch(BASE_URL + "/auth/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(user)
    });

    if (response.ok) {
        alert("Registration Successful");
        window.location.href = "index.html";
    } else {
        alert("Registration Failed");
    }
}

// Login
async function login() {

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const response = await fetch(BASE_URL + "/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "include",
        body: JSON.stringify({
            email,
            password
        })
    });

    if (response.ok) {

        const token = response.headers.get("Authorization");
        const userId = response.headers.get("User-Id");

        localStorage.setItem("token", token);
        localStorage.setItem("userId", userId);

        const message = await response.text();
        alert(message);

        window.location.href = "products.html";

    } else {

        const error = await response.text();
        alert(error);
    }
}