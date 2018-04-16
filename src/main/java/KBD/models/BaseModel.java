package KBD.models;

import KBD.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseModel {
    public static void executeUpdate(String query) {
        try {
            Connection connection = Database.getConnection();

            Statement statement = connection.createStatement();
            statement.executeUpdate(query);

            connection.close();
        } catch (SQLException e) {
            Logger.error(e.getMessage());
        }
    }
}
