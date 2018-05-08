package KBD.v1.controllers.user;

import KBD.Config;
import KBD.models.IndividualUser;
import KBD.v1.controllers.BaseHttpServlet;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@WebServlet("/api/v1/login")
public class Login extends BaseHttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JSONObject reqData = parseJsonData(request);
        java.util.List<String> requiredFields = Arrays.asList(
                "username", "password"
        );
        jsonValidation(reqData, requiredFields);

        String username = reqData.getString("username");
        String password = reqData.getString("password");
        IndividualUser user = IndividualUser.find(username, password);

        if(user != null) {
            try {
                Date exp = new Date(System.currentTimeMillis() + Config.EXPIRE_TIME);
                Algorithm algorithm = Algorithm.HMAC256(Config.SECRET);
                String token = JWT.create()
                        .withClaim("userId", user.getId())
                        .withIssuer("auth0")
                        .withExpiresAt(exp)
                        .sign(algorithm);

                List<String> attributes = Arrays.asList("id", "name", "balance");
                JSONObject data = new JSONObject();
                data.put("message", "ورود موفقیت‌آمیز بود");
                data.put("user", user.toJson(attributes));
                data.put("token", token);

                sendJsonResponse(response, data);
            } catch (UnsupportedEncodingException exception){
                //UTF-8 encoding not supported
            } catch (JWTCreationException exception){
                //Invalid Signing configuration / Couldn't convert Claims.
            }
        } else {
            errorResponse(response, HttpServletResponse.SC_FORBIDDEN, "ورود ناموفق بود.");
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
