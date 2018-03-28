package KBD.v1.controllers.auth;


import KBD.Database;
import KBD.models.IndividualUser;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/v1/auth/login")
public class Login extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        IndividualUser user = Database.getUser(0);

        JSONObject userJson = new JSONObject();
        userJson.put("id", user.getId());
        userJson.put("name", user.getName());
        userJson.put("balance", user.getBalance());

        PrintWriter out = response.getWriter();
        out.print(userJson.toString());
        out.flush();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
