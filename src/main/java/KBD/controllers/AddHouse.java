package KBD.controllers;

import KBD.Database;
import KBD.models.House;
import KBD.models.Logger;
import KBD.models.enums.BuildingType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/add-house")
public class AddHouse extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        House newHouse;
        boolean isBuyingHouse = request.getParameter("deal-type").equals("0");

        try {
            if (isBuyingHouse)
                newHouse = new House(
                        BuildingType.parseBuildingType(request.getParameter("building-type")),
                        Integer.parseInt(request.getParameter("area")),
                        request.getParameter("address"),
                        Float.parseFloat(request.getParameter("price")),
                        request.getParameter("phone"),
                        request.getParameter("description")
                );
            else
                newHouse = new House(
                        BuildingType.parseBuildingType(request.getParameter("building-type")),
                        Integer.parseInt(request.getParameter("area")),
                        request.getParameter("address"),
                        0,
                        Float.parseFloat(request.getParameter("price")),
                        request.getParameter("phone"),
                        request.getParameter("description")
                );
            Database.addHouse(newHouse);
            request.setAttribute("message", "خانه با موفقیت اضافه شد.");
        } catch (Exception e) {
            Logger.error(e.getMessage());
            request.setAttribute("message", "خطا در ایجاد خانه رخ داد.");
        }

        getServletContext().getRequestDispatcher("/index.jsp").forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}
