<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="KBD.models.IndividualUser" %>
<header>
    <%
        IndividualUser user = (IndividualUser) request.getAttribute("user");
    %>
    <div> نام کاربر: <%= user.getName()%> </div>
    <div>اعتبار: <%= user.getBalance()%> تومان </div>
</header>

<%
    if (request.getAttribute("message") != null) {
%>
<h4><%= request.getAttribute("message") %></h4>
<%
    }
%>