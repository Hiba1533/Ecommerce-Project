const BASE_URL = "http://localhost:8080";
const userId = 1;

window.onload = loadCart;

// Helper function to get auth headers
function getAuthHeaders() {
    const token = localStorage.getItem('token');
    return {
        'Authorization': token ? `Bearer ${token}` : '',
        'Content-Type': 'application/json'
    };
}

// Helper function to handle API responses
async function handleResponse(response) {
    if (!response.ok) {
        if (response.status === 403) {
            throw new Error('Access denied. Please login again.');
        }
        if (response.status === 404) {
            return null; // Return null for 404 (cart not found)
        }
        throw new Error(`HTTP error! status: ${response.status}`);
    }

    // Check if response has content
    const text = await response.text();
    if (!text) {
        return null;
    }

    try {
        return JSON.parse(text);
    } catch (e) {
        console.error('Failed to parse JSON:', text);
        return null;
    }
}

async function loadCart() {
    try {
        const response = await fetch(
            BASE_URL + "/cart/" + userId,
            {
                method: 'GET',
                headers: getAuthHeaders()
            }
        );

        const items = await handleResponse(response);

        // If cart is empty or null, show empty cart message
        if (!items || items.length === 0) {
            document.getElementById("cartItems").innerHTML = `
                <div class="empty-cart">
                    <p>Your cart is empty</p>
                    <a href="/products">Continue Shopping</a>
                </div>
            `;
            return;
        }

        let html = "";
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
                <br>
                <button onclick="updateQuantity(${item.id})">
                    Update
                </button>
                <button onclick="deleteItem(${item.id})">
                    Delete
                </button>
            </div>
            `;
        });

        document.getElementById("cartItems").innerHTML = html;

    } catch (error) {
        console.error('Error loading cart:', error);
        document.getElementById("cartItems").innerHTML = `
            <div class="error-message">
                <p>Error loading cart: ${error.message}</p>
                <button onclick="loadCart()">Retry</button>
            </div>
        `;
    }
}

async function updateQuantity(id) {
    try {
        const quantity = document.getElementById("qty" + id).value;

        if (!quantity || quantity < 1) {
            alert('Please enter a valid quantity');
            return;
        }

        const response = await fetch(
            BASE_URL + "/cart/items/" + id + "?quantity=" + quantity,
            {
                method: "PUT",
                headers: getAuthHeaders()
            }
        );

        if (response.ok) {
            alert("Quantity Updated");
            await loadCart(); // Use await to ensure cart reloads after update
        } else {
            const error = await handleResponse(response);
            alert(error?.message || 'Failed to update quantity');
        }
    } catch (error) {
        console.error('Error updating quantity:', error);
        alert('Error updating quantity: ' + error.message);
    }
}

async function deleteItem(id) {
    try {
        if (!confirm('Are you sure you want to delete this item?')) {
            return;
        }

        const response = await fetch(
            BASE_URL + "/cart/items/" + id,
            {
                method: "DELETE",
                headers: getAuthHeaders()
            }
        );

        if (response.ok) {
            alert("Item Deleted");
            await loadCart(); // Use await to ensure cart reloads after deletion
        } else {
            const error = await handleResponse(response);
            alert(error?.message || 'Failed to delete item');
        }
    } catch (error) {
        console.error('Error deleting item:', error);
        alert('Error deleting item: ' + error.message);
    }
}