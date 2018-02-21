package KBD.controllers;

import KBD.Config;
import KBD.Database;
import KBD.models.House;
import KBD.models.IndividualUser;
import KBD.models.Logger;
import KBD.models.enums.HouseOwner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/pay")
public class Pay extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IndividualUser user = (IndividualUser) request.getAttribute("user");

        HouseOwner houseOwner = HouseOwner.parseString(request.getParameter("owner"));
        String houseId = request.getParameter("id");

        House house = Database.getHouse(houseOwner, houseId);

        if (user.pay(house))
            request.setAttribute("message", "مبلغ ۱۰۰۰ تومان برای دریافت شماره‌ی مالک/مشاور از حساب شما کسر شد");
        else
            request.setAttribute("message", "اعتبار شما برای دریافت شماره‌ی مالک/مشاور کافی نیست.");

        request.setAttribute("house", house);
        getServletContext().getRequestDispatcher("/house-leaf.jsp").forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
