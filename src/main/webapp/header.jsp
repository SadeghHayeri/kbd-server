<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<header>
    <a href="<c:url value="/"/>">خانه</a><br>
    <div> نام کاربر: <c:out value="${user.name}"/> </div>
    <div>اعتبار: <c:out value="${user.balance}"/> تومان </div>
</header>

<c:if test="${not empty message}">
    <h4><c:out value="${message}"/></h4>
</c:if>