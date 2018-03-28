package KBD.v1.controllers.house;

import KBD.Database;
import KBD.models.House;
import KBD.models.enums.BuildingType;
import KBD.models.enums.DealType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/search")
public class List extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BuildingType buildingType = BuildingType.parseBuildingType(request.getParameter("building-type"));
        DealType dealType = DealType.parseDealType(request.getParameter("deal-type"));

        int minimumArea = 0;
        if (!request.getParameter("minimum-area").isEmpty())
            minimumArea = Integer.parseInt(request.getParameter("minimum-area"));

        float maximumPrice = Float.MAX_VALUE;
        if (!request.getParameter("maximum-price").isEmpty())
            maximumPrice = Float.parseFloat(request.getParameter("maximum-price"));

        ArrayList<House> houses = Database.getHouses(minimumArea, buildingType, dealType, maximumPrice);

        request.setAttribute("houses", houses);
        getServletContext().getRequestDispatcher("/house-list.jsp").forward(request,response);
    }
}
