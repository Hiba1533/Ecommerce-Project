const BASE_URL = "http://localhost:8080";

const userId = localStorage.getItem("userId");
const token = localStorage.getItem("token");

window.onload = function () {

    if (!userId) {
        alert("Please login first");
        window.location.href = "index.html";
        return;
    }

    loadTotal();
    loadOrders();
};

async function loadTotal() {

    const response = await fetch(
        `${BASE_URL}/cart/total/${userId}`,
        {
            headers: {
                "Authorization": token
            }
        }
    );

    if (response.ok) {

        const total = await response.json();
        document.getElementById("totalAmount").innerHTML = total;

    } else {

        alert("Unable to load total.");

    }
}

async function placeOrder() {

    const order = {

        name: document.getElementById("name").value,
        phone: document.getElementById("phone").value,
        address: document.getElementById("address").value

    };

    const response = await fetch(
        `${BASE_URL}/orders/${userId}`,
        {
            method: "POST",
            headers: {
                "Authorization": "Bearer" + token,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(order)
        }
    );

    if (response.ok) {

        alert("Order placed successfully!");

        document.getElementById("name").value = "";
        document.getElementById("phone").value = "";
        document.getElementById("address").value = "";

        loadTotal();
        loadOrders();

    } else {

        alert(await response.text());

    }
}

async function loadOrders() {

    const response = await fetch(
        `${BASE_URL}/orders/${userId}`,
        {
            headers: {
                "Authorization": token
            }
        }
    );

    if (!response.ok) {

        alert("Unable to load orders");
        return;

    }

    const orders = await response.json();

    console.log("Orders:", orders);

    let html = "";

    orders.forEach(order => {

        html += `

        <div class="card">

            <h3>Order ID: ${order.id}</h3>

            <p>
                <b>Total Amount:</b>
                Rs. ${order.amount}
            </p>

            <p>
                <b>Order Status:</b>
                ${order.status}
            </p>

            <p>
                <b>Payment Status:</b>
                ${order.paymentStatus}
            </p>

            ${
            order.paymentStatus === "paid"
            || order.paymentStatus === "PAID"
                ? `
                    <p style="color:green;">
                        <b>Payment Completed ✓</b>
                    </p>
                  `
                : `
                    <button onclick="payOrder(${order.id})">
                        Pay
                    </button>
                  `
        }

        </div>

        `;

    });

    document.getElementById("orders").innerHTML = html;
}

async function payOrder(orderId) {

    const response = await fetch(
        `${BASE_URL}/orders/${orderId}/pay`,
        {
            method: "PUT",
            headers: {
                "Authorization": token
            }
        }
    );

    if (response.ok) {

        alert("Payment successful!");

        loadOrders();

    } else {

        alert("Payment failed!");

    }
}