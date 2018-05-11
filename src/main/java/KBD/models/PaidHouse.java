package KBD.models;


import KBD.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

public class PaidHouse extends BaseModel {
    private int userId;
    public int houseOwner;
    public String houseId;

    public PaidHouse(int userId, int houseOwner, String houseId) {
        this.userId = userId;
        this.houseOwner = houseOwner;
        this.houseId = houseId;
    }

    public void save() {
        executeUpdate(
                String.format("INSERT INTO %s VALUES (%d, ?, %d)", Database.PAID_HOUSES_TB, userId, houseOwner),
                Arrays.asList(houseId)
        );
    }
}
