package KBD.v1.controllers.auth;

import KBD.models.IndividualUser;
import KBD.Config;
import KBD.models.Logger;
import KBD.v1.controllers.BaseHttpServlet;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@WebServlet("/api/v1/auth/charge")
public class Charge extends BaseHttpServlet {

    private boolean charge(int userId, float value) {
        try {
            String bankUrl = Config.BANK_URL;
            String apiKey = Config.API_KEY;

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(bankUrl);

            JSONObject data = new JSONObject();
            data.put("userId", userId);
            data.put("value", value);
            StringEntity postString = new StringEntity(data.toString());

            post.setEntity(postString);
            post.setHeader("Content-type", "application/json");
            post.setHeader("apiKey", apiKey);

            HttpResponse response = httpClient.execute(post);
            return (response.getStatusLine().getStatusCode() == Config.SUCCESS_RESPONSE_STATUS_CODE);
        }
        catch (Exception e) {
            Logger.error(e.getMessage());
            return false;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IndividualUser user = (IndividualUser) request.getAttribute("user");
        JSONObject data = parseJsonData(request);
        List<String> requiredFields = Arrays.asList("balance-value");

        if(jsonValidation(response, data, requiredFields)) {
            int balanceValue = data.getInt("balance-value");

            if (charge(user.getId(), balanceValue)) {
                user.addBalance(balanceValue);
                successResponse(response,"افزایش موجودی با موفقیت انجام شد.");
            } else {
                errorResponse(response, HttpServletResponse.SC_SERVICE_UNAVAILABLE, "در حال حاضر سرور بانک در دسترس نمی باشد.");
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}
