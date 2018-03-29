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
import java.util.Arrays;

@WebServlet("/api/v1/house")
public class Main extends BaseHttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject data = parseJsonData(request);
        java.util.List<String> requiredFields = Arrays.asList(
                "building-type", "deal-type", "area", "address", "phone", "description",
                "buy-price" ,"base-price", "rent-price"
        );

        if(jsonValidation(response, data, requiredFields)) {
            House newHouse;
            boolean isBuyingHouse = data.getString("deal-type").equals("0");
            if (isBuyingHouse)
                newHouse = new House(
                        BuildingType.parseBuildingType(data.getString("building-type")),
                        data.getInt("area"),
                        data.getString("address"),
                        Float.parseFloat(data.getString("buy-price")),
                        data.getString("phone"),
                        data.getString("description")
                );
            else
                newHouse = new House(
                        BuildingType.parseBuildingType(data.getString("building-type")),
                        data.getInt("area"),
                        data.getString("address"),
                        Float.parseFloat(data.getString("base-price")),
                        Float.parseFloat(data.getString("rent-price")),
                        data.getString("phone"),
                        data.getString("description")
                );
            Database.addHouse(newHouse);
            successResponse(response, "خانه با موفقیت اضافه شد.");
        }
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
        java.util.List<String> attributes = Arrays.asList(
                "id", "owner", "area", "address", "buildingType", "dealType", "imgURL", "price"
        );

        JSONArray data = new JSONArray();
        for (House house : houses) {
            JSONObject jsonHouse = house.toJson(attributes);
            data.put(jsonHouse);
        }

        sendJsonResponse(response, data);
    }
}
