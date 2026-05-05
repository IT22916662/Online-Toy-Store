<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Toy - Online Toy Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">Online Toy Store</a>
        <div class="ms-auto">
            <a href="${pageContext.request.contextPath}/toys" class="btn btn-outline-light btn-sm">Browse Toys</a>
        </div>
    </div>
</nav>

<div class="container py-4">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm">
                <div class="card-header bg-success text-white">
                    <h4 class="mb-0">Add New Toy</h4>
                </div>
                <div class="card-body">

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">${error}</div>
                    </c:if>

                    <form method="post" action="${pageContext.request.contextPath}/add-toy" id="toyForm">

                        <div class="row mb-3">
                            <div class="col-md-4">
                                <label class="form-label">Toy ID</label>
                                <input type="text" class="form-control" name="id"
                                       value="${not empty formId ? formId : suggestedId}" required>
                                <small class="text-muted">Suggested: ${suggestedId}</small>
                            </div>
                            <div class="col-md-8">
                                <label class="form-label">Toy Name</label>
                                <input type="text" class="form-control" name="name"
                                       value="${formName}" required>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Category</label>
                            <select class="form-select" name="type" id="typeSelect" required>
                                <option value="">-- Select category --</option>
                                <option value="ELECTRONIC"  ${formType == 'ELECTRONIC'  ? 'selected' : ''}>Electronic Toy</option>
                                <option value="EDUCATIONAL" ${formType == 'EDUCATIONAL' ? 'selected' : ''}>Educational Toy</option>
                                <option value="SOFT"        ${formType == 'SOFT'        ? 'selected' : ''}>Soft Toy</option>
                            </select>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-4">
                                <label class="form-label">Price (LKR)</label>
                                <input type="number" step="0.01" min="0" class="form-control"
                                       name="price" value="${formPrice}" required>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">Stock</label>
                                <input type="number" min="0" class="form-control"
                                       name="stock" value="${formStock}" required>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">Age Group</label>
                                <input type="text" class="form-control" name="ageGroup"
                                       placeholder="e.g. 4+" value="${formAgeGroup}" required>
                            </div>
                        </div>

                        <div class="mb-3" id="extraGroup">
                            <label class="form-label" id="extraLabel">Extra Information</label>
                            <input type="text" class="form-control" name="extra"
                                   id="extraInput" value="${formExtra}">
                            <small class="text-muted" id="extraHint"></small>
                        </div>

                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-success">Save Toy</button>
                            <a href="${pageContext.request.contextPath}/toys" class="btn btn-secondary">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    // Adjust the "extra" field label/hint based on the chosen category.
    const typeSelect  = document.getElementById('typeSelect');
    const extraLabel  = document.getElementById('extraLabel');
    const extraInput  = document.getElementById('extraInput');
    const extraHint   = document.getElementById('extraHint');

    function updateExtraField() {
        switch (typeSelect.value) {
            case 'ELECTRONIC':
                extraLabel.textContent = 'Needs Battery? (true / false)';
                extraInput.placeholder = 'true';
                extraHint.textContent  = 'Type "true" if the toy uses batteries, otherwise "false".';
                break;
            case 'EDUCATIONAL':
                extraLabel.textContent = 'Skill Type';
                extraInput.placeholder = 'e.g. Logic, Math, Language';
                extraHint.textContent  = 'The skill this toy helps develop.';
                break;
            case 'SOFT':
                extraLabel.textContent = 'Material';
                extraInput.placeholder = 'e.g. Cotton, Polyester';
                extraHint.textContent  = 'Filling or fabric used.';
                break;
            default:
                extraLabel.textContent = 'Extra Information';
                extraInput.placeholder = '';
                extraHint.textContent  = 'Pick a category first.';
        }
    }
    typeSelect.addEventListener('change', updateExtraField);
    updateExtraField();
</script>

</body>
</html>
