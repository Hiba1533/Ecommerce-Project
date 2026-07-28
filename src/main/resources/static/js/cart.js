const BASE_URL = "http://localhost:8080";

function getAuthHeaders() {
    const token = localStorage.getItem("token");

    return {
        "Content-Type": "application/json",
        "Authorization": token
    };
}

const userId = localStorage.getItem("userId");

// Add to Cart
async function addToCart(productId) {

    const response = await fetch(
        `${BASE_URL}/cart/items?userId=${userId}&productId=${productId}&quantity=1`,
        {
            method: "POST",
            headers: getAuthHeaders()
        }
    );

    if (response.ok) {
        alert("Added to Cart");
        loadCart();
        loadTotal();
    } else {
        alert(await response.text());
    }
}

// Load Cart
async function loadCart() {

    const response = await fetch(
        `${BASE_URL}/cart/items/${userId}`,
        {
            method: "GET",
            headers: getAuthHeaders()
        }
    );

    if (!response.ok) {
        alert("Error : " + response.status);
        return;
    }

    const items = await response.json();

    let html = "";

    if (items.length === 0) {

        html = "<h2>Your Cart is Empty</h2>";

    } else {

        items.forEach(item => {

            html += `
                <div class="card">
                    <h3>${item.product.name}</h3>
                    <p>Price : Rs. ${item.product.price}</p>
                    <p>Quantity : ${item.quantity}</p>

                    <input
                        id="qty${item.id}"
                        type="number"
                        value="${item.quantity}"
                        min="1">

                    <br><br>

                    <button onclick="updateQuantity(${item.id})">
                        Update
                    </button>

                    <button onclick="deleteItem(${item.id})">
                        Delete
                    </button>

                    <hr>
                </div>
            `;
        });
    }

    document.getElementById("cartItems").innerHTML = html;
}

// Update Quantity
async function updateQuantity(cartItemId) {

    const quantity =
        document.getElementById("qty" + cartItemId).value;

    const response = await fetch(
        `${BASE_URL}/cart/items/${cartItemId}?quantity=${quantity}`,
        {
            method: "PUT",
            headers: getAuthHeaders()
        }
    );

    if (response.ok) {
        loadCart();
        loadTotal();
    } else {
        alert(await response.text());
    }
}

// Delete Item
async function deleteItem(cartItemId) {

    const response = await fetch(
        `${BASE_URL}/cart/items/${cartItemId}`,
        {
            method: "DELETE",
            headers: getAuthHeaders()
        }
    );

    if (response.ok) {
        loadCart();
        loadTotal();
    } else {
        alert(await response.text());
    }
}

// Cart Total
async function loadTotal() {

    const response = await fetch(
        `${BASE_URL}/cart/total/${userId}`,
        {
            method: "GET",
            headers: getAuthHeaders()
        }
    );

    if (!response.ok) {
        return;
    }

    const total = await response.text();

    document.getElementById("total").innerHTML =
        "Total : Rs. " + total;
}

// Clear Cart
async function clearCart() {

    const response = await fetch(
        `${BASE_URL}/cart/${userId}`,
        {
            method: "DELETE",
            headers: getAuthHeaders()
        }
    );

    if (response.ok) {
        alert("Cart Cleared");
        loadCart();
        loadTotal();
    } else {
        alert(await response.text());
    }
}

// Load cart when page opens
window.onload = function () {

    if (!userId) {
        alert("Please login first");
        window.location.href = "index.html";
        return;
    }

    loadCart();
    loadTotal();
};