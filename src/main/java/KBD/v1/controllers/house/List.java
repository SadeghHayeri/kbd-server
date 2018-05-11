package KBD.v1.controllers.house;

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
public class List extends BaseHttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject data = parseJsonData(request);
        java.util.List<String> requiredFields = Arrays.asList(
                "building-type", "deal-type", "area", "address", "phone", "description", "price"
        );
        jsonValidation(data, requiredFields);

        House newHouse;
        boolean isBuyingHouse = data.getString("deal-type").equals("0");
        JSONObject priceData = data.getJSONObject("price");
        if (isBuyingHouse) {
            jsonValidation(priceData, Arrays.asList("buy"));

            newHouse = new House(
                BuildingType.parseBuildingType(data.getString("building-type")),
                data.getInt("area"),
                data.getString("address"),
                priceData.getInt("buy"),
                data.getString("phone"),
                data.getString("description")
            );
        } else {
            jsonValidation(data.getJSONObject("price"), Arrays.asList("base", "rent"));

            newHouse = new House(
                BuildingType.parseBuildingType(data.getString("building-type")),
                data.getInt("area"),
                data.getString("address"),
                priceData.getInt("base"),
                priceData.getInt("rent"),
                data.getString("phone"),
                data.getString("description")
            );
        }

        newHouse.save();

        java.util.List<String> attributes = Arrays.asList(
                "id", "owner"
        );
        JSONObject jsonHouse = newHouse.toJson(attributes);
        successResponse(response, "خانه با موفقیت اضافه شد.", jsonHouse);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BuildingType buildingType = BuildingType.parseBuildingType(request.getParameter("building-type"));
        DealType dealType = DealType.parseDealType(request.getParameter("deal-type"));

        int minimumArea = 0;
        if (!(request.getParameter("minimum-area") == null))
            minimumArea = Integer.parseInt(request.getParameter("minimum-area"));

        int maximumPrice = Integer.MAX_VALUE;
        if (!(request.getParameter("maximum-price") == null))
            maximumPrice = Integer.parseInt(request.getParameter("maximum-price"));

        ArrayList<House> houses = House.filter(minimumArea, buildingType, dealType, maximumPrice);
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
