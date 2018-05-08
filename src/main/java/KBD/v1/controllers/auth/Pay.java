package KBD.v1.controllers.auth;

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
import java.util.List;

@WebServlet("/api/v1/auth/pay")
public class Pay extends BaseHttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        checkAuth(request);
        IndividualUser user = (IndividualUser) request.getAttribute("user");

        JSONObject data = parseJsonData(request);
        jsonValidation(data, Arrays.asList("owner", "id"));

        HouseOwner houseOwner;
        try {
            houseOwner = HouseOwner.parseString(data.getString("owner"));
        } catch (NotFoundException e) {
            errorResponse(response, HttpServletResponse.SC_NOT_FOUND, "مالک این خانه در سیستم وجود ندارد.");
            return;
        }

        String houseId = data.getString("id");

        House house;
        try {
            house = Database.getHouse(houseOwner, houseId);
        } catch (NotFoundException e) {
            errorResponse(response, HttpServletResponse.SC_NOT_FOUND, "این خانه وجود ندارد.");
            return;
        }

        if (user.hasPaid(house)) {
            errorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "شما قبلا این پرداخت را انجام داده‌اید.");
        } else if (user.pay(house)) {
            user.save();
            java.util.List<String> attributes = Arrays.asList(
                    "phone", "hasBoughtPhone"
            );
            JSONObject houseData = house.toJson(attributes, user);
            successResponse(response, "مبلغ ۱۰۰۰ تومان برای دریافت شماره مالک/مشاور از حساب شما کسر شد", houseData);
        } else {
            errorResponse(response, HttpServletResponse.SC_PAYMENT_REQUIRED, "اعتبار شما برای دریافت شماره مالک/مشاور کافی نیست.");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
