<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sitemesh" uri="http://www.sitemesh.org/sitemesh3" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><sitemesh:write property="title"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/app.css" rel="stylesheet">
    <sitemesh:write property="head"/>
</head>
<body class="bg-light">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
        <div class="container">
            <a class="navbar-brand fw-semibold" href="${pageContext.request.contextPath}/login">Bài tập 03</a>
            <div class="navbar-nav ms-auto align-items-lg-center gap-lg-2">
                <c:choose>
                    <c:when test="${not empty sessionScope.currentUser}">
                        <span class="navbar-text text-white">
                            Xin chào, <strong><c:out value="${sessionScope.currentUser.fullname}"/></strong>
                        </span>
                        <a class="nav-link text-white" href="${pageContext.request.contextPath}/profile">Hồ sơ</a>
                        <form method="post" action="${pageContext.request.contextPath}/logout" class="d-inline">
                            <button class="btn btn-outline-light btn-sm" type="submit">Đăng xuất</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <a class="nav-link text-white" href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                        <a class="nav-link text-white" href="${pageContext.request.contextPath}/register">Đăng ký</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </nav>
    <main class="container py-5">
        <sitemesh:write property="body"/>
    </main>
    <footer class="border-top bg-white py-3">
        <div class="container small text-secondary">Quản lý hồ sơ người dùng · JPA · Sitemesh 3</div>
    </footer>
</body>
</html>
