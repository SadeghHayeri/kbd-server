package KBD.models;


import KBD.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PaidHouse extends BaseModel {
    private int userId;
    public int houseOwner;
    public String houseId;

    public PaidHouse(int houseOwner, String houseId) {
        this.houseOwner = houseOwner;
        this.houseId = houseId;
    }

    public void save() {
        try {
            Connection connection = Database.getConnection();
            String SQL = String.format("INSERT INTO %s VALUES (?, ?, ?)", Database.PAID_HOUSES_TB);
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setInt(1, userId);
            pstmt.setString(2, houseId);
            pstmt.setInt(3, houseOwner);
            pstmt.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            Logger.error(e.getMessage());
        }
    }
}
