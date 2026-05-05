<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Hely - Toy Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark app-navbar">
    <div class="container">
        <a class="navbar-brand" href="index.jsp">Hely</a>
        <div class="ms-auto d-flex gap-2">
            <a href="${pageContext.request.contextPath}/toys" class="btn btn-outline-light btn-sm">Catalog</a>
            <a href="${pageContext.request.contextPath}/add-toy" class="btn btn-light btn-sm text-primary fw-semibold">Add Toy</a>
        </div>
    </div>
</nav>

<div class="hero-section">
    <div class="container">
        <div class="row align-items-center g-4">
            <div class="col-lg-6">
                <span class="hero-kicker">Online toy store</span>
                <h1 class="display-4 fw-bold text-white mb-3">Hely Toy Management System</h1>
                <p class="lead text-white-75 mb-4">Keep your toy catalog, prices, stock levels, and categories ready for real store operations.</p>
                <div class="d-flex flex-wrap gap-3">
                    <a href="${pageContext.request.contextPath}/toys" class="btn btn-light btn-lg text-primary fw-semibold">Browse Catalog</a>
                    <a href="${pageContext.request.contextPath}/add-toy" class="btn btn-outline-light btn-lg">Add New Toy</a>
                </div>
            </div>
            <div class="col-lg-6">
                <img class="hero-image" src="${pageContext.request.contextPath}/img/a.jpeg" alt="Hely toy store display">
            </div>
        </div>
    </div>
</div>

<div class="container py-5">
    <div class="store-strip">
        <img src="${pageContext.request.contextPath}/img/f.jpeg" alt="Soft toys collection">
        <div>
            <h2>Find the best toys for your kid</h2>
            <p>Hely keeps daily product management simple with quick actions, clean inventory tables, and clear stock status.</p>
            <a href="${pageContext.request.contextPath}/toys" class="btn btn-outline-primary">Open Inventory</a>
        </div>
    </div>

    <div class="row g-3 mt-4">
        <div class="col-md-4">
            <div class="stat-tile">
                <span>01</span>
                <strong>Manage stock</strong>
                <small>Track availability before shelves run low.</small>
            </div>
        </div>
        <div class="col-md-4">
            <div class="stat-tile">
                <span>02</span>
                <strong>Organize categories</strong>
                <small>Separate electronic, educational, and soft toys.</small>
            </div>
        </div>
        <div class="col-md-4">
            <div class="stat-tile">
                <span>03</span>
                <strong>Update prices</strong>
                <small>Keep catalog prices ready for customers.</small>
            </div>
        </div>
    </div>
</div>

<%@ include file="WEB-INF/views/footer.jspf" %>

</body>
</html>
