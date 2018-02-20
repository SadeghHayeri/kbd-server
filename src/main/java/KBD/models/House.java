package KBD.models;

import KBD.Config;
import KBD.models.enums.BuildingType;
import KBD.models.enums.DealType;

/**
 * Created by sadegh on 2/12/18.
 */
public class House {
    static private int idCounter;

    static {
        idCounter = 1;
    }

    private int id;

    public int getId() {
        return id;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public int getArea() {
        return area;
    }

    public String getAddress() {
        return address;
    }

    public String getImageURL() {
        return imageURL;
    }

    public DealType getDealType() {
        return dealType;
    }

    public float getBasePrice() {
        return basePrice;
    }

    public float getRentPrice() {
        return rentPrice;
    }

    public float getSellPrice() {
        return sellPrice;
    }

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }

    public String getExpireTime() {
        return expireTime;
    }

    private BuildingType buildingType;
    private int area;
    private String address;
    private String imageURL;
    private DealType dealType;
    private float basePrice;
    private float rentPrice;
    private float sellPrice;
    private String phone;
    private String description;
    private String expireTime;

    public House(BuildingType buildingType, int area, String address, float sellPrice, String phone, String description) {
        this.dealType = DealType.BUY;

        this.id = (idCounter++);
        this.buildingType = buildingType;
        this.area = area;
        this.address = address;
        this.sellPrice = sellPrice;
        this.phone = phone;
        this.description = description;
        this.imageURL = Config.NO_IMAGE_PATH;
    }

    public House(BuildingType buildingType, int area, String address, float basePrice, float rentPrice, String phone, String description) {
        this.dealType = DealType.RENTAL;

        this.id = (idCounter++);
        this.buildingType = buildingType;
        this.area = area;
        this.address = address;
        this.basePrice = basePrice;
        this.rentPrice = rentPrice;
        this.phone = phone;
        this.description = description;
        this.imageURL = Config.NO_IMAGE_PATH;
    }
}
