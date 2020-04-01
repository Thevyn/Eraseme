$( function() {
    getAllCustomer();
});


function getAllCustomer() {
    $.get( "/api/getCustomers", (data) => {
        showCustomer(data);
    });
}

function showCustomer(customer) {
    // lag tabell

    let ut =
        "<table class='table '><tr>" +
        "<th>CreditCard</th><th>CustomerNo</th><th>Name</th><th>Email</th>" +
        "<th>Password</th><th>Phone</th>" +
        "</tr>";
    for (const c of customer) {
        ut += "<tr>";
        ut +=
            "<td>" +
            c.creditCard +
            "</td><td>" +
            c.customerNo +
            "</td><td>" +
            c.name +
            "</td><td>" +
            c.email +
            "</td>" +
            "<td>" +
            c.password +
            "</td><td>" +
            c.phone +
            "</td>";
        ut += "</tr>";
    }
    ut += "</table>";

    $("#customers").html(ut);
}

function eraseCustomer(e) {
    e.preventDefault();

    var customerNo = $("#customerNo").val();
    $.get("/api/Erase_CUSTOMER?custnum="+customerNo);

    $("#customerNo").val("")
    getAllCustomer();

}