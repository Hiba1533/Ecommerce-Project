const BASE_URL = "http://localhost:8080";

const userId = 1;

window.onload = loadOrders;

async function placeOrder() {

    const order = {
        name: document.getElementById("name").value,
        phone: document.getElementById("phone").value,
        address: document.getElementById("address").value,
        amount: document.getElementById("amount").value
    };

    const response = await fetch(BASE_URL + "/orders/" + userId, {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(order)

    });

    if (response.ok) {

        alert("Order Placed Successfully");

        loadOrders();

    } else {

        alert("Unable to Place Order");

    }
}

async function loadOrders() {

    const response = await fetch(BASE_URL + "/orders/" + userId);

    const orders = await response.json();

    let html = "";

    orders.forEach(order => {

        html += `
        <div class="order">

            <p><b>Order ID:</b> ${order.id}</p>

            <p><b>Name:</b> ${order.name}</p>

            <p><b>Amount:</b> Rs. ${order.amount}</p>

            <p><b>Status:</b> ${order.status}</p>

            <button id="pay-${order.id}" onclick="pay(${order.id})">
                Pay Now
            </button>

        </div>
        `;

    });

    document.getElementById("orders").innerHTML = html;
}

function pay(orderId) {

    const btn = document.getElementById("pay-" + orderId);

    btn.innerHTML = "Payment Successful ✓";
    btn.style.backgroundColor = "green";
    btn.disabled = true;

    alert("Payment Successful");
}