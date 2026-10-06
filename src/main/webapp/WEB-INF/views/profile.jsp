<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Hồ sơ cá nhân</title>
</head>
<body>
<div class="row justify-content-center">
    <div class="col-12 col-lg-8">
        <section class="card border-0 shadow-sm">
            <div class="card-body p-4 p-md-5">
                <div class="d-flex align-items-center gap-3 mb-4">
                    <c:choose>
                        <c:when test="${not empty user.imageFile}">
                            <img class="profile-avatar" alt="Ảnh đại diện"
                                 src="${pageContext.request.contextPath}/profile-image/<c:out value='${user.imageFile}'/>">
                        </c:when>
                        <c:otherwise>
                            <div class="profile-avatar profile-avatar-placeholder" aria-hidden="true">
                                <c:out value="${user.username.substring(0, 1).toUpperCase()}"/>
                            </div>
                        </c:otherwise>
                    </c:choose>
                    <div>
                        <h1 class="h3 mb-1">Hồ sơ cá nhân</h1>
                        <p class="mb-0 text-secondary">@<c:out value="${user.username}"/></p>
                    </div>
                </div>
                <c:if test="${param.updated == 'true'}">
                    <div class="alert alert-success" role="status">Thông tin hồ sơ đã được cập nhật.</div>
                </c:if>
                <form method="post" action="${pageContext.request.contextPath}/profile"
                      enctype="multipart/form-data" novalidate>
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
                    <div class="mb-4">
                        <label class="form-label" for="avatar">Ảnh đại diện</label>
                        <input class="form-control ${not empty errors.avatar ? 'is-invalid' : ''}"
                               id="avatar" name="avatar" type="file" accept="image/png,image/jpeg,image/gif">
                        <div class="form-text">Chấp nhận PNG, JPEG hoặc GIF; dung lượng tối đa 5 MB.</div>
                        <c:if test="${not empty errors.avatar}">
                            <div class="invalid-feedback d-block"><c:out value="${errors.avatar}"/></div>
                        </c:if>
                    </div>
                    <div class="d-flex flex-column flex-sm-row gap-2">
                        <button class="btn btn-primary" type="submit">Lưu thay đổi</button>
                        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/profile">Hủy</a>
                    </div>
                </form>
            </div>
        </section>
    </div>
</div>
</body>
</html>
