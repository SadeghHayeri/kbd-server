package KBD.v1.controllers.auth;

import KBD.models.IndividualUser;
import KBD.v1.controllers.BaseHttpServlet;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/api/v1/auth/profile")
public class Profile extends BaseHttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        checkAuth(request);
        IndividualUser user = (IndividualUser) request.getAttribute("user");

        List<String> attributes = Arrays.asList("id", "name", "username", "phone", "balance", "isAdmin");
        JSONObject data = user.toJson(attributes);

        sendJsonResponse(response, data);
    }
}
