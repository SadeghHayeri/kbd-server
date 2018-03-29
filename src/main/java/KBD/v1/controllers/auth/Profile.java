package KBD.v1.controllers.auth;

import KBD.models.IndividualUser;
import KBD.v1.controllers.BaseHttpServlet;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/auth/profile")
public class Profile extends BaseHttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IndividualUser user = (IndividualUser) request.getAttribute("user");

        JSONObject data = new JSONObject();
        data.put("id", user.getId());
        data.put("name", user.getName());
        data.put("username", user.getUsername());
        data.put("phone", user.getPhone());
        data.put("balance", user.getBalance());

        sendJsonResponse(response, data);
    }
}
