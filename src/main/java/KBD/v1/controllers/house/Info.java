package KBD.v1.controllers.house;

import KBD.Database;
import KBD.models.House;
import KBD.models.enums.HouseOwner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/info/*")
public class Info extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] path = request.getPathInfo().split("/");

        HouseOwner houseOwner = HouseOwner.parseString(path[1]);
        String houseId = path[2];

        House house = Database.getHouse(houseOwner, houseId);

        request.setAttribute("house", house);
        getServletContext().getRequestDispatcher("/house-leaf.jsp").forward(request,response);
    }
}
