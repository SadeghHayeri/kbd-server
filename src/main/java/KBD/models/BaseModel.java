package KBD.models;

import KBD.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseModel {
    protected final static int NOT_INITIATE = -1;
    protected boolean isSaved = false;

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
