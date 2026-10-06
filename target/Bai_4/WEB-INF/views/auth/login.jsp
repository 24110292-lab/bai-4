<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Đăng nhập</title>
</head>
<body>
<div class="row justify-content-center">
    <div class="col-12 col-md-7 col-lg-5">
        <section class="card border-0 shadow-sm">
            <div class="card-body p-4 p-md-5">
                <h1 class="h3 mb-2">Đăng nhập</h1>
                <p class="text-secondary mb-4">Đăng nhập để xem và cập nhật hồ sơ.</p>
                <c:if test="${not empty loginError}">
                    <div class="alert alert-danger" role="alert"><c:out value="${loginError}"/></div>
                </c:if>
                <form method="post" action="${pageContext.request.contextPath}/login" novalidate>
                    <div class="mb-3">
                        <label class="form-label" for="username">Tên đăng nhập</label>
                        <input class="form-control ${not empty errors.username ? 'is-invalid' : ''}"
                               id="username" name="username" autocomplete="username" required maxlength="50"
                               value="<c:out value='${form.username}'/>">
                        <c:if test="${not empty errors.username}">
                            <div class="invalid-feedback"><c:out value="${errors.username}"/></div>
                        </c:if>
                    </div>
                    <div class="mb-4">
                        <label class="form-label" for="password">Mật khẩu</label>
                        <input class="form-control ${not empty errors.password ? 'is-invalid' : ''}"
                               id="password" name="password" type="password" autocomplete="current-password"
                               required maxlength="60">
                        <c:if test="${not empty errors.password}">
                            <div class="invalid-feedback"><c:out value="${errors.password}"/></div>
                        </c:if>
                    </div>
                    <button class="btn btn-primary w-100" type="submit">Đăng nhập</button>
                </form>
                <p class="mb-0 mt-4 text-center">Chưa có tài khoản?
                    <a href="${pageContext.request.contextPath}/register">Đăng ký</a>
                </p>
            </div>
        </section>
    </div>
</div>
</body>
</html>
