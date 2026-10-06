<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Tạo tài khoản</title>
</head>
<body>
<div class="row justify-content-center">
    <div class="col-12 col-md-8 col-lg-6">
        <section class="card border-0 shadow-sm">
            <div class="card-body p-4 p-md-5">
                <h1 class="h3 mb-2">Tạo tài khoản</h1>
                <p class="text-secondary mb-4">Đăng ký để quản lý hồ sơ cá nhân.</p>
                <form method="post" action="${pageContext.request.contextPath}/register" novalidate>
                    <div class="mb-3">
                        <label class="form-label" for="username">Tên đăng nhập</label>
                        <input class="form-control ${not empty errors.username ? 'is-invalid' : ''}"
                               id="username" name="username" autocomplete="username" required maxlength="50"
                               value="<c:out value='${form.username}'/>">
                        <c:if test="${not empty errors.username}">
                            <div class="invalid-feedback"><c:out value="${errors.username}"/></div>
                        </c:if>
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="fullname">Họ và tên</label>
                        <input class="form-control ${not empty errors.fullname ? 'is-invalid' : ''}"
                               id="fullname" name="fullname" autocomplete="name" required maxlength="100"
                               value="<c:out value='${form.fullname}'/>">
                        <c:if test="${not empty errors.fullname}">
                            <div class="invalid-feedback"><c:out value="${errors.fullname}"/></div>
                        </c:if>
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="phone">Số điện thoại</label>
                        <input class="form-control ${not empty errors.phone ? 'is-invalid' : ''}"
                               id="phone" name="phone" type="tel" autocomplete="tel" required maxlength="20"
                               value="<c:out value='${form.phone}'/>">
                        <c:if test="${not empty errors.phone}">
                            <div class="invalid-feedback"><c:out value="${errors.phone}"/></div>
                        </c:if>
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="password">Mật khẩu</label>
                        <input class="form-control ${not empty errors.password ? 'is-invalid' : ''}"
                               id="password" name="password" type="password" autocomplete="new-password"
                               required minlength="8" maxlength="60">
                        <c:if test="${not empty errors.password}">
                            <div class="invalid-feedback"><c:out value="${errors.password}"/></div>
                        </c:if>
                    </div>
                    <div class="mb-4">
                        <label class="form-label" for="confirmPassword">Xác nhận mật khẩu</label>
                        <input class="form-control ${not empty errors.confirmPassword ? 'is-invalid' : ''}"
                               id="confirmPassword" name="confirmPassword" type="password"
                               autocomplete="new-password" required minlength="8" maxlength="60">
                        <c:if test="${not empty errors.confirmPassword}">
                            <div class="invalid-feedback"><c:out value="${errors.confirmPassword}"/></div>
                        </c:if>
                    </div>
                    <button class="btn btn-primary w-100" type="submit">Đăng ký</button>
                </form>
                <p class="mb-0 mt-4 text-center">Đã có tài khoản?
                    <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                </p>
            </div>
        </section>
    </div>
</div>
</body>
</html>
