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

    const user = {
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    };

    const response = await fetch(BASE_URL + "/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(user)
    });

    if (response.ok) {

        // Get JWT token from response header
        const token = response.headers.get("Authorization");

        if (token) {
            localStorage.setItem("token", token);
        }

        alert("Login Successful");

        window.location.href = "products.html";

    } else {
        alert("Invalid Email or Password");
    }
}