<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Online Toy Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
        <a class="navbar-brand" href="index.jsp">Online Toy Store</a>
    </div>
</nav>

<div class="container py-5">
    <div class="p-5 mb-4 bg-light rounded-3 text-center">
        <h1 class="display-5 fw-bold">Toy Management System</h1>
        <p class="lead">Manage the toy catalog: add, browse, edit, and remove toys.</p>
    </div>

    <div class="row g-4">
        <div class="col-md-4">
            <div class="card h-100 shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">Add New Toy</h5>
                    <p class="card-text">Add a new toy to the catalog.</p>
                    <a href="add-toy" class="btn btn-success">Add Toy</a>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card h-100 shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">Browse Toys</h5>
                    <p class="card-text">View, search, edit, and delete existing toys.</p>
                    <a href="toys" class="btn btn-primary">View Toys</a>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card h-100 shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">About</h5>
                    <p class="card-text">SE1020 OOP Project — Toy Management Component.</p>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
