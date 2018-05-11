package KBD.v1.controllers.house;

import KBD.Database;
import KBD.models.IndividualUser;
import KBD.models.Logger;
import KBD.v1.controllers.BaseHttpServlet;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@WebServlet("/api/v1/houses")
public class Report extends BaseHttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        checkAuth(request);

        IndividualUser user = (IndividualUser) request.getAttribute("user");

        if (user.isAdmin())
            sendJsonResponse(response, listAllInfo());
        else
            sendJsonResponse(response, listUserInfo(user));
    }

    private JSONObject listAllInfo() {
        JSONObject users = new JSONObject();
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(
                    String.format(
                            "SELECT * FROM %s U " +
                                    "LEFT JOIN %s PH ON U.id = PH.user_id " +
                                    "LEFT JOIN %s R ON R.id = PH.house_owner " +
                                    "ORDER BY U.id",
                            Database.INDIVIDUAL_USERS_TB, Database.PAID_HOUSES_TB, Database.REALSTATE_USERS_TB
                    )
            );

            while (resultSet.next()) {
                JSONObject data;
                if (resultSet.getString(12) != null) {
                     data = new JSONObject();
                    data.put("house_owner", resultSet.getString(12));
                    data.put("house_id", resultSet.getString("house_id"));
                    users.append(resultSet.getString("username"), data);
                } else
                    users.put(resultSet.getString("username"), new ArrayList<>());

            }

            connection.close();
        } catch (SQLException e) {
            Logger.error(e.getMessage());
        }
        return users;
    }

    private JSONArray listUserInfo(IndividualUser user) {
        JSONArray paidHouses = new JSONArray();
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(
                    String.format(
                            "SELECT * FROM %s PH " +
                            "INNER JOIN %s R ON R.id = PH.house_owner " +
                            "WHERE PH.user_id = %d ",
                            Database.PAID_HOUSES_TB, Database.REALSTATE_USERS_TB, user.getId()
                    )
            );

            while (resultSet.next()) {
                JSONObject data = new JSONObject();
                data.put("house_owner", resultSet.getString(5));
                data.put("house_id", resultSet.getString("house_id"));

                paidHouses.put(data);
            }

            connection.close();
        } catch (SQLException e) {
            Logger.error(e.getMessage());
        }
        return paidHouses;
    }
}
