package KBD.controllers;

import KBD.models.IndividualUser;
import KBD.Config;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/charge")
public class Charge extends HttpServlet {

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
            return false;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("balance-value").isEmpty()) {
            request.setAttribute("message", "فیلد اعتبار ضروری است.");
        } else {
            IndividualUser user = (IndividualUser) request.getAttribute("user");
            float balanceValue = Float.parseFloat(request.getParameter("balance-value"));

            if (charge(user.getId(), balanceValue)) {
                user.addBalance(balanceValue);
                request.setAttribute("message", "افزایش موجودی با موفقیت انجام شد.");
            } else
                request.setAttribute("message", "خطا در افزایش موجودی رخ داد.");
        }

        getServletContext().getRequestDispatcher("/index.jsp").forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}
