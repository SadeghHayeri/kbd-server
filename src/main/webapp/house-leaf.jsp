<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page import="KBD.models.enums.*" %>
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
    <title>نمایش خانه</title>
    <style>
        * {
            direction: rtl;
        }
    </style>
</head>
<body>
    <jsp:include page="header.jsp"/>

    <div>
        <img src="${house.imageURL}" alt=""><br>
        <ul>
            <li>نوع ساختمان:
                <c:if test="${house.buildingType == BuildingType.VILLA}">ویلایی</c:if>
                <c:if test="${house.buildingType == BuildingType.APARTMENT}">آپارتمان</c:if>
            </li>
            <li>نوع قرارداد:
                <c:if test="${house.dealType == DealType.BUY}">فروش</c:if>
                <c:if test="${house.dealType == DealType.RENTAL}">اجاره</c:if>
            </li>
            <li> متراژ: <c:out value="${house.area}"/></li>
            <c:if test="${house.dealType == DealType.BUY}">
                <li> قیمت: <c:out value="${house.sellPrice}"/></li>
            </c:if>
            <c:if test="${house.dealType == DealType.RENTAL}">
                <li> قیمت پایه: <c:out value="${house.basePrice}"/></li>
                <li> مبلغ اجاره: <c:out value="${house.rentPrice}"/></li>
            </c:if>
            <li> آدرس: <c:out value="${house.address}"/></li>
            <li> توضیحات: <c:out value="${house.description}"/></li>
        </ul
        <form action="<c:url value="/pay"/>" method="POST">
            <input type="hidden" name="owner" value="${house.owner.toString()}">
            <input type="hidden" name="id" value="${house.id}">
            <input type="submit" value="دریافت شماره‌ی مالک/مشاور">
        </form>
    </div>

</body>
</html>
