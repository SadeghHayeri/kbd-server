package KBD.v1.controllers.user;

import KBD.Database;
import KBD.models.IndividualUser;
import KBD.v1.controllers.BaseHttpServlet;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/login")
public class Login extends BaseHttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IndividualUser user = Database.getUser(0);

        JSONObject data = new JSONObject();
        data.put("id", user.getId());
        data.put("name", user.getName());
        data.put("balance", user.getBalance());

        sendJsonResponse(response, data);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
