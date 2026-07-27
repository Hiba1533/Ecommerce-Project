const BASE_URL = "http://localhost:8080";
const userId = 1; // Change later after login

window.onload = loadProducts;

async function loadProducts() {

    const response = await fetch(BASE_URL + "/product");

    const products = await response.json();

    let html = "";

    products.forEach(product => {

        html += `
<div class="card">

    <img
        src="${BASE_URL}/product/image/${product.id}"
        alt="${product.name}"
        class="product-image">

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

    <button onclick="addToCart(${product.id})">
        Add To Cart
    </button>

</div>
`;    });

    document.getElementById("products").innerHTML = html;
}

async function addToCart(productId) {

    const quantity = document.getElementById("qty" + productId).value;

    const response = await fetch(
        `${BASE_URL}/cart/items?userId=${userId}&productId=${productId}&quantity=${quantity}`,
        {
            method: "POST"
        }
    );

    if (response.ok) {
        alert("Product Added Successfully");
    } else {
        alert("Failed to Add Product");
    }
}