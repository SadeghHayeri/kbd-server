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
            pstmt.setString(1, String.valueOf(userId));
            pstmt.setString(2, houseId);
            pstmt.setString(3, String.valueOf(houseOwner));
            pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
