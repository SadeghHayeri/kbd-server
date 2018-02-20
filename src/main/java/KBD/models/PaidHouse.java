package KBD.models;

import KBD.models.enums.HouseOwner;

public class PaidHouse {
    public HouseOwner houseOwner;
    public String houseId;

    public PaidHouse(HouseOwner houseOwner, String houseId) {
        this.houseOwner = houseOwner;
        this.houseId = houseId;
    }
}
