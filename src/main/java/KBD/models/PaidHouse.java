package KBD.models;


public class PaidHouse extends BaseModel {
    private static final String TABLE_NAME = "paid_houses";
    private int userId;
    public int houseOwner;
    public String houseId;

    public PaidHouse(int houseOwner, String houseId) {
        this.houseOwner = houseOwner;
        this.houseId = houseId;
    }

    public void save() {
        executeUpdate(
                String.format("INSERT INTO %s VALUES (%d, '%s', %d)", TABLE_NAME, userId, houseId, houseOwner)
        );
    }
}
