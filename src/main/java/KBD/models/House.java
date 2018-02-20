package KBD.models;

import KBD.Config;

/**
 * Created by sadegh on 2/12/18.
 */
public class House {
    static private int idCounter;

    static {
        idCounter = 1;
    }

    private int id;
    private int buildingType;
    private int area;
    private String address;
    private String imageURL;
    private int dealType;
    private float basePrice;
    private float rentPrice;
    private float sellPrice;
    private String phone;
    private String description;
    private String expireTime;

    public House(int buildingType, int area, String address, float sellPrice, String phone, String description) {
        this.id = (idCounter++);
        this.buildingType = buildingType;
        this.area = area;
        this.address = address;
        this.dealType = 1;
        this.sellPrice = sellPrice;
        this.phone = phone;
        this.description = description;
        this.imageURL = Config.NO_IMAGE_PATH;
    }

    public House(int buildingType, int area, String address, float basePrice, float rentPrice, String phone, String description) {
        this.id = (idCounter++);
        this.buildingType = buildingType;
        this.area = area;
        this.address = address;
        this.dealType = 0;
        this.basePrice = basePrice;
        this.rentPrice = rentPrice;
        this.phone = phone;
        this.description = description;
        this.imageURL = Config.NO_IMAGE_PATH;
    }
}
