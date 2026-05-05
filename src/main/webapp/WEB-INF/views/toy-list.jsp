<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Toy Catalog - Hely</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark app-navbar">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">Hely</a>
        <div class="ms-auto">
            <a href="${pageContext.request.contextPath}/add-toy" class="btn btn-light btn-sm text-primary fw-semibold">+ Add Toy</a>
        </div>
    </div>
</nav>

<div class="container py-4">
    <div class="catalog-header mb-4">
        <div>
            <span class="section-kicker">Hely inventory</span>
            <h1>Toy Catalog</h1>
            <p>Search, update, and maintain products for the online toy store.</p>
        </div>
        <img src="${pageContext.request.contextPath}/img/a.jpeg" alt="Toy store aisle">
    </div>

    <c:choose>
        <c:when test="${msg == 'added'}">
            <div class="alert alert-success">Toy added successfully.</div>
        </c:when>
        <c:when test="${msg == 'updated'}">
            <div class="alert alert-success">Toy updated successfully.</div>
        </c:when>
        <c:when test="${msg == 'deleted'}">
            <div class="alert alert-warning">Toy deleted.</div>
        </c:when>
        <c:when test="${msg == 'notfound'}">
            <div class="alert alert-danger">Toy not found. Nothing was deleted.</div>
        </c:when>
    </c:choose>

    <div class="card mb-4 shadow-sm">
        <div class="card-body">
            <h5 class="card-title">Search Toys</h5>
            <form method="get" action="${pageContext.request.contextPath}/toys" class="row g-2">
                <div class="col-md-5">
                    <input type="text" class="form-control" name="name"
                           placeholder="Search by name..." value="${nameQuery}">
                </div>
                <div class="col-md-4">
                    <select class="form-select" name="category">
                        <option value=""            ${category == ''            ? 'selected' : ''}>All Categories</option>
                        <option value="ELECTRONIC"  ${category == 'ELECTRONIC'  ? 'selected' : ''}>Electronic</option>
                        <option value="EDUCATIONAL" ${category == 'EDUCATIONAL' ? 'selected' : ''}>Educational</option>
                        <option value="SOFT"        ${category == 'SOFT'        ? 'selected' : ''}>Soft</option>
                    </select>
                </div>
                <div class="col-md-3 d-flex gap-2">
                    <button type="submit" class="btn btn-primary flex-grow-1">Search</button>
                    <a href="${pageContext.request.contextPath}/toys" class="btn btn-outline-secondary">Reset</a>
                </div>
            </form>
        </div>
    </div>

    <div class="card shadow-sm">
        <div class="card-body">
            <h5 class="card-title mb-3">Toy Catalog (<c:out value="${toys.size()}"/>)</h5>

            <c:choose>
                <c:when test="${empty toys}">
                    <div class="text-muted">No toys match your search. Try resetting the filters
                        or <a href="${pageContext.request.contextPath}/add-toy">add a new toy</a>.</div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-hover align-middle">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Category</th>
                                    <th>Age</th>
                                    <th>Price (LKR)</th>
                                    <th>Discounted</th>
                                    <th>Stock</th>
                                    <th>Extra</th>
                                    <th class="text-end">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="t" items="${toys}">
                                    <tr>
                                        <td><strong>${t.id}</strong></td>
                                        <td>${t.name}</td>
                                        <td><span class="badge bg-secondary">${t.category}</span></td>
                                        <td>${t.ageGroup}</td>
                                        <td><fmt:formatNumber value="${t.price}" type="number" minFractionDigits="2"/></td>
                                        <td class="text-primary fw-semibold">
                                            <fmt:formatNumber value="${t.discountedPrice}" type="number" minFractionDigits="2"/>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${t.stock == 0}">
                                                    <span class="badge bg-danger">Out of stock</span>
                                                </c:when>
                                                <c:when test="${t.stock < 5}">
                                                    <span class="badge bg-warning text-dark">${t.stock} (low)</span>
                                                </c:when>
                                                <c:otherwise>
                                                    ${t.stock}
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${t.extraField}</td>
                                        <td class="text-end">
                                            <a class="btn btn-sm btn-outline-primary"
                                               href="${pageContext.request.contextPath}/edit-toy?id=${t.id}">Edit</a>
                                            <a class="btn btn-sm btn-outline-danger"
                                               href="${pageContext.request.contextPath}/delete-toy?id=${t.id}"
                                               onclick="return confirm('Delete toy ${t.id} - ${t.name}?');">Delete</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<%@ include file="footer.jspf" %>

</body>
</html>
