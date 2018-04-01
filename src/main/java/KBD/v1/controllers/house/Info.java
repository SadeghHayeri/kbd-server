package KBD.v1.controllers.house;

import KBD.Database;
import KBD.models.House;
import KBD.models.IndividualUser;
import KBD.models.enums.HouseOwner;
import KBD.v1.Exceptions.NotFoundException;
import KBD.v1.controllers.BaseHttpServlet;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@WebServlet("/api/v1/house/*")
public class Info extends BaseHttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] path = request.getPathInfo().split("/");

        if (path.length != 3) {
            errorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "خطا در فیلدهای مورد نیاز.");
            return;
        }

        //TODO: Use multiple exceptions!
        try {
            HouseOwner houseOwner = HouseOwner.parseString(path[1]);
            String houseId = path[2];

            try {
                House house = Database.getHouse(houseOwner, houseId);
                IndividualUser user = (IndividualUser) request.getAttribute("user");

                java.util.List<String> attributes = Arrays.asList(
                        "id", "owner", "area", "address", "buildingType", "dealType", "imgURL", "description", "price", "phone", "hasBoughtPhone"
                );
                JSONObject data = house.toJson(attributes, user);

                sendJsonResponse(response, data);
            } catch (NotFoundException e) {
                errorResponse(response, HttpServletResponse.SC_NOT_FOUND, "این خانه وجود ندارد.");
            }

        } catch (NotFoundException e) {
            errorResponse(response, HttpServletResponse.SC_NOT_FOUND, "مالک این خانه در سیستم وجود ندارد.");
        }
    }
}
