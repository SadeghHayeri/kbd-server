package KBD.models;

import KBD.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class BaseModel {
    protected final static int NOT_INITIATE = -1;
    protected boolean isSaved = false;
    protected boolean isModified = false;

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

    public static void executeUpdate(String query, List<String> parameters) {
        try {
            Connection connection = Database.getConnection();

            PreparedStatement pStatement = connection.prepareStatement(query);
            for (int i = 0; i < parameters.size(); i++)
                pStatement.setString(i+1, parameters.get(i));
            pStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            Logger.error(e.getMessage());
        }
    }
}
