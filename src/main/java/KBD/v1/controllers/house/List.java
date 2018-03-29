package KBD.v1.controllers.house;

import KBD.Database;
import KBD.models.House;
import KBD.models.enums.BuildingType;
import KBD.models.enums.DealType;
import KBD.v1.controllers.BaseHttpServlet;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/api/v1/house")
public class List extends BaseHttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BuildingType buildingType = BuildingType.parseBuildingType(request.getParameter("building-type"));
        DealType dealType = DealType.parseDealType(request.getParameter("deal-type"));

        int minimumArea = 0;
        if (!(request.getParameter("minimum-area") == null))
            minimumArea = Integer.parseInt(request.getParameter("minimum-area"));

        float maximumPrice = Float.MAX_VALUE;
        if (!(request.getParameter("maximum-price") == null))
            maximumPrice = Float.parseFloat(request.getParameter("maximum-price"));

        ArrayList<House> houses = Database.getHouses(minimumArea, buildingType, dealType, maximumPrice);

        JSONArray data = new JSONArray();
        for (House house : houses) {
            JSONObject jsonHouse = new JSONObject();
            jsonHouse.put("id", house.getId());
            jsonHouse.put("owner", house.getOwner());
            jsonHouse.put("area", house.getArea());
            jsonHouse.put("address", house.getAddress());
            jsonHouse.put("buildingType", house.getBuildingType());
            jsonHouse.put("imgURL", house.getImageURL());
            jsonHouse.put("dealType", house.getDealType());

            JSONObject price = new JSONObject();
            if(house.getDealType() == DealType.BUY) {
                price.put("sell", house.getSellPrice());
            } else {
                price.put("base", house.getBasePrice());
                price.put("rent", house.getRentPrice());
            }
            jsonHouse.put("price", price);
            data.put(jsonHouse);
        }

        sendJsonResponse(response, data);
    }
}
