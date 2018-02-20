package KBD.controllers;

import KBD.Database;
import KBD.models.House;
import KBD.models.Logger;
import KBD.models.enums.HouseOwner;
import org.apache.http.entity.StringEntity;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

@WebServlet("/info/*")
public class HouseInfo extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger.info("searched! " + request.getPathInfo());
        String[] path = request.getPathInfo().split("/");

        HouseOwner houseOwner = HouseOwner.parseString(path[1]);
        String houseId = path[2];

        House house = Database.getHouse(houseOwner, houseId);
    }
}