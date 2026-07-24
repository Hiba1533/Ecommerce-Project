const BASE_URL = "http://localhost:8080";

const userId = 1;

window.onload = loadCart;

async function loadCart(){

    const response = await fetch(
        BASE_URL + "/cart/items/" + userId
    );

    const items = await response.json();

    let html = "";

    items.forEach(item=>{

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

}

async function updateQuantity(id){

    const quantity =
        document.getElementById("qty"+id).value;

    const response = await fetch(

        BASE_URL+
        "/cart/items/"+id+"?quantity="+quantity,

        {
            method:"PUT"
        }

    );

    if(response.ok){

        alert("Quantity Updated");

        loadCart();

    }

}

async function deleteItem(id){

    const response = await fetch(

        BASE_URL+"/cart/items/"+id,

        {
            method:"DELETE"
        }

    );

    if(response.ok){

        alert("Item Deleted");

        loadCart();

    }

}