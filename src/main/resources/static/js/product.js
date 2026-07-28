const BASE_URL = "http://localhost:8080";

const userId = localStorage.getItem("userId");

window.onload = function () {

    if (!userId) {
        alert("Please login first");
        window.location.href = "index.html";
        return;
    }

    loadProducts();
};

async function loadProducts() {

    const response = await fetch(BASE_URL + "/product");

    if (!response.ok) {
        alert("Unable to load products");
        return;
    }

    const products = await response.json();

    let html = "";

    products.forEach(product => {

        html += `
        <div class="card">

            <h2>${product.name}</h2>

            <p>${product.description}</p>

            <p><b>Price:</b> Rs. ${product.price}</p>

            <p><b>Stock:</b> ${product.stock}</p>

            <p><b>Category:</b> ${product.category.name}</p>

            <input
                type="number"
                id="qty${product.id}"
                value="1"
                min="1">

            <br><br>

            <button onclick="addToCart(${product.id})">
                Add To Cart
            </button>

        </div>
        `;
    });

    document.getElementById("products").innerHTML = html;
}

async function addToCart(productId) {

    const quantity = document.getElementById("qty" + productId).value;

    const token = localStorage.getItem("token");

    const response = await fetch(
        `${BASE_URL}/cart/items?userId=${userId}&productId=${productId}&quantity=${quantity}`,
        {
            method: "POST",
            headers: {
                "Authorization": token
            }
        }
    );

    if (response.ok) {
        alert("Product Added Successfully");
    } else {
        alert(await response.text());
    }
}