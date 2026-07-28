const BASE_URL = "http://localhost:8080";

window.onload = loadCategories;

async function loadCategories() {

    const response = await fetch(BASE_URL + "/category");

    const categories = await response.json();

    let options = "";

    categories.forEach(category => {

        options += `
            <option value="${category.id}">
                ${category.name}
            </option>
        `;
    });

    document.getElementById("category").innerHTML = options;
}

async function addCategory() {

    const name = document.getElementById("categoryName").value;

    const response = await fetch(BASE_URL + "/category", {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify({
            name: name
        })

    });

    if (response.ok) {

        alert("Category Added");

        document.getElementById("categoryName").value = "";

        loadCategories();

    } else {

        alert("Failed");
    }
}

async function addProduct() {

    const product = {
        name: document.getElementById("name").value,
        description: document.getElementById("description").value,
        price: document.getElementById("price").value,
        stock: document.getElementById("stock").value,
        category: {
            id: document.getElementById("category").value
        }
    };

    const response = await fetch(BASE_URL + "/product", {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(product)

    });

    if (response.ok) {

        alert("Product Added Successfully");

        document.getElementById("name").value = "";
        document.getElementById("description").value = "";
        document.getElementById("price").value = "";
        document.getElementById("stock").value = "";

    } else {

        const error = await response.text();
        alert(error);
    }
}