package KBD.models;

import KBD.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PaidHouse {
    private static final String TABLE_NAME = "paid_houses";
    private int userId;
    public int houseOwner;
    public String houseId;

    public static void create(int userId, String houseId, int houseOwner) {
        try {
            Connection connection = Database.getConnection();

            Statement statement = connection.createStatement();
            statement.executeUpdate( String.format(
                    "INSERT INTO %s VALUES (%d, '%s', %d)", TABLE_NAME, userId, houseId, houseOwner)
            );

            connection.close();
        } catch (SQLException e) {
            Logger.error(e.getMessage());
        }
    }

    public PaidHouse(int houseOwner, String houseId) {
        this.houseOwner = houseOwner;
        this.houseId = houseId;
    }
}
