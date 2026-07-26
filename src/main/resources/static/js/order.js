const BASE_URL = "http://localhost:8080";

const userId = 1;

window.onload = function () {

    loadOrders();

    loadCartTotal();

};


async function loadCartTotal() {

    const response = await fetch(BASE_URL + "/cart/total/" + userId);

    const total = await response.text();

    document.getElementById("totalAmount").innerHTML = total;

}



async function placeOrder() {

    const order = {

        name: document.getElementById("name").value,

        phone: document.getElementById("phone").value,

        address: document.getElementById("address").value

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


        document.getElementById("name").value = "";

        document.getElementById("phone").value = "";

        document.getElementById("address").value = "";


        loadOrders();

        loadCartTotal();

    }

    else {

        alert("Unable to Place Order");

    }

}



async function loadOrders() {

    const response = await fetch(BASE_URL + "/orders/" + userId);

    const orders = await response.json();


    let html = "";


    orders.forEach(order => {


        html += `

        <div class="order" id="order-${order.id}">


            <p><b>Order ID:</b> ${order.id}</p>


            <p><b>Name:</b> ${order.name}</p>


            <p><b>Amount:</b> Rs. ${order.amount}</p>


            <p><b>Status:</b> ${order.status}</p>


            <button 
                id="pay-${order.id}" 
                onclick="pay(${order.id}, ${order.amount})">

                Pay Rs. ${order.amount}

            </button>


        </div>

        `;


    });


    document.getElementById("orders").innerHTML = html;

}





function pay(orderId, amount) {


    fetch(BASE_URL + "/orders/" + orderId + "/pay", {

        method: "PUT"

    })


        .then(response => response.json())


        .then(data => {


            alert(
                "Order Placed Successfully!\n\nPayment Successful ✓\nAmount Paid: Rs. "
                + amount
            );


            // Remove order from My Orders screen
            document.getElementById("order-" + orderId).remove();


        })


        .catch(error => {


            alert("Payment Failed");


            console.log(error);


        });


}