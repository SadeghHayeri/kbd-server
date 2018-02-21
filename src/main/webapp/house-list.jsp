<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page import="KBD.models.enums.DealType" %>
<%--
  Created by IntelliJ IDEA.
  User: mamareza
  Date: 2/20/2018 AD
  Time: 14:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>جست‌و‌جو</title>
    <style>
        * {
            direction: rtl;
        }
    </style>
</head>
<body>
    <jsp:include page="header.jsp"/>

    <div>
        تعداد خانه‌های پیدا شده: <c:out value="${houses.size()}"/>
    </div>

    <div>
        <c:forEach var="house" items="${houses}">
            <img src="${house.imageURL}" alt=""><br>
            <ul>
                <c:if test="${house.dealType == DealType.BUY}">
                    <li> قیمت: <c:out value="${house.sellPrice}"/></li>
                </c:if>
                <c:if test="${house.dealType == DealType.RENTAL}">
                    <li> قیمت پایه: <c:out value="${house.basePrice}"/></li>
                    <li> مبلغ اجاره: <c:out value="${house.rentPrice}"/></li>
                </c:if>
                <li> متراژ: <c:out value="${house.area}"/></li>
                <li>نوع:
                    <c:if test="${house.dealType == DealType.BUY}">فروش</c:if>
                    <c:if test="${house.dealType == DealType.RENTAL}">رهن و اجاره</c:if>
                </li>
            </ul>
            <a href="<c:url value="/info/${house.owner.toString()}/${house.id}"/>">مشاهده جزئیات</a>
            <hr>
        </c:forEach>
    </div>

</body>
</html>
