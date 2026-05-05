<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Toy - Online Toy Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">Online Toy Store</a>
        <div class="ms-auto">
            <a href="${pageContext.request.contextPath}/toys" class="btn btn-outline-light btn-sm">Back to Catalog</a>
        </div>
    </div>
</nav>

<div class="container py-4">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm">
                <div class="card-header bg-primary text-white">
                    <h4 class="mb-0">Edit Toy &mdash; ${toy.id}</h4>
                </div>
                <div class="card-body">

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">${error}</div>
                    </c:if>

                    <form method="post" action="${pageContext.request.contextPath}/edit-toy">

                        <input type="hidden" name="id" value="${toy.id}">
                        <input type="hidden" name="type" value="${toy.category}">

                        <div class="row mb-3">
                            <div class="col-md-4">
                                <label class="form-label">Toy ID</label>
                                <input type="text" class="form-control" value="${toy.id}" disabled>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">Category</label>
                                <input type="text" class="form-control" value="${toy.category}" disabled>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">Age Group</label>
                                <input type="text" class="form-control" name="ageGroup"
                                       value="${not empty formAgeGroup ? formAgeGroup : toy.ageGroup}" required>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Toy Name</label>
                            <input type="text" class="form-control" name="name"
                                   value="${not empty formName ? formName : toy.name}" required>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="form-label">Price (LKR)</label>
                                <input type="number" step="0.01" min="0" class="form-control"
                                       name="price" value="${not empty formPrice ? formPrice : toy.price}" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Stock</label>
                                <input type="number" min="0" class="form-control"
                                       name="stock" value="${not empty formStock ? formStock : toy.stock}" required>
                            </div>
                        </div>

                        <div class="mb-3">
                            <c:choose>
                                <c:when test="${toy.category == 'ELECTRONIC'}">
                                    <label class="form-label">Needs Battery? (true / false)</label>
                                </c:when>
                                <c:when test="${toy.category == 'EDUCATIONAL'}">
                                    <label class="form-label">Skill Type</label>
                                </c:when>
                                <c:when test="${toy.category == 'SOFT'}">
                                    <label class="form-label">Material</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="form-label">Extra</label>
                                </c:otherwise>
                            </c:choose>
                            <input type="text" class="form-control" name="extra"
                                   value="${not empty formExtra ? formExtra : toy.extraField}" required>
                        </div>

                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-primary">Save Changes</button>
                            <a href="${pageContext.request.contextPath}/toys" class="btn btn-secondary">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
