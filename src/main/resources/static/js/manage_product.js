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

    const name = document.getElementById("name").value;
    const description = document.getElementById("description").value;
    const price = document.getElementById("price").value;
    const stock = document.getElementById("stock").value;
    const categoryId = document.getElementById("category").value;

    const imageFile = document.getElementById("imageFile").files[0];

    const product = {
        name: name,
        description: description,
        price: price,
        stock: stock,
        category: {
            id: categoryId
        }
    };

    const formData = new FormData();

    formData.append(
        "product",
        new Blob(
            [JSON.stringify(product)],
            { type: "application/json" }
        )
    );

    formData.append("imageFile", imageFile);

    const response = await fetch(BASE_URL + "/product", {
        method: "POST",
        body: formData
    });

    if (response.ok) {

        alert("Product Added Successfully");

        document.getElementById("name").value = "";
        document.getElementById("description").value = "";
        document.getElementById("price").value = "";
        document.getElementById("stock").value = "";
        document.getElementById("imageFile").value = "";

    } else {

        const error = await response.text();
        alert(error);

    }
}