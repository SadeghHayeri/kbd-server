package KBD.controllers;

import KBD.models.IndividualUser;
import KBD.Config;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/pay")
public class Pay extends HttpServlet {

    private boolean pay(int userId, float value) {
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
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IndividualUser user = (IndividualUser) request.getAttribute("user");
        float balanceValue = Float.parseFloat(request.getParameter("balance-value"));

        if (pay(user.getId(), balanceValue)) {
            user.addBalance(balanceValue);
            request.setAttribute("message", "افزایش موجودی با موفقیت انجام شد.");
        } else
            request.setAttribute("message", "خطا در افزایش موجودی رخ داد.");

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
        dispatcher.forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}
