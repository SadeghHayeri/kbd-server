package KBD.models;

import KBD.Config;
import KBD.models.enums.BuildingType;
import KBD.models.enums.DealType;
import KBD.models.enums.HouseOwner;
import KBD.v1.services.JSONService;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by sadegh on 2/12/18.
 */
public class House {
    static private int idCounter;

    static {
        idCounter = 1;
    }

    private String id;
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
    private HouseOwner owner;

    public House(BuildingType buildingType, int area, String address, float sellPrice, String phone, String description) {
        this.dealType = DealType.BUY;

        this.owner = HouseOwner.SYSTEM;
        this.id = Integer.toString(idCounter++);
        this.buildingType = buildingType;
        this.area = area;
        this.address = address;
        this.sellPrice = sellPrice;
        this.phone = phone;
        this.description = description;
        this.imageURL = Config.NO_IMAGE_PATH;
    }

    public House(HouseOwner owner, String id, BuildingType buildingType, int area, String address, float sellPrice, String phone, String description, String imageURL) {
        this.dealType = DealType.BUY;

        this.owner = owner;
        this.id = id;
        this.buildingType = buildingType;
        this.area = area;
        this.address = address;
        this.sellPrice = sellPrice;
        this.phone = phone;
        this.description = description;
        this.imageURL = imageURL;
    }

    public House(BuildingType buildingType, int area, String address, float basePrice, float rentPrice, String phone, String description) {
        this.dealType = DealType.RENTAL;

        this.owner = HouseOwner.SYSTEM;
        this.id = Integer.toString(idCounter++);
        this.buildingType = buildingType;
        this.area = area;
        this.address = address;
        this.basePrice = basePrice;
        this.rentPrice = rentPrice;
        this.phone = phone;
        this.description = description;
        this.imageURL = Config.NO_IMAGE_PATH;
    }

    public House(HouseOwner owner, String id, BuildingType buildingType, int area, String address, float basePrice, float rentPrice, String phone, String description, String imageURL) {
        this.dealType = DealType.RENTAL;

        this.owner = owner;
        this.id = id;
        this.buildingType = buildingType;
        this.area = area;
        this.address = address;
        this.basePrice = basePrice;
        this.rentPrice = rentPrice;
        this.phone = phone;
        this.description = description;
        this.imageURL = imageURL;
    }

    public String getId() {
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

    public String getHiddenPhone() {
        return phone.substring(0, 1) + "*****" + phone.substring(6);
    }

    public String getDescription() {
        return description;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public HouseOwner getOwner() {
        return owner;
    }

    public JSONObject toJson (List<String> keys) {
        JSONObject data = new JSONObject();
        data.put("id", id);
        data.put("owner", owner);
        data.put("area", area);
        data.put("address", address);
        data.put("buildingType", buildingType);
        data.put("imgURL", imageURL);
        data.put("dealType", dealType);
        data.put("description", description);

        JSONObject price = new JSONObject();
        if(dealType == DealType.BUY) {
            price.put("sell", sellPrice);
        } else {
            price.put("base", basePrice);
            price.put("rent", rentPrice);
        }
        data.put("price", price);

        data = JSONService.keepAllowedKeys(data, keys);
        return data;
    }

    public JSONObject toJson (List<String> keys, IndividualUser user) {
        JSONObject data = toJson(keys);

        if (user.hasPaid(this)) {
            data.put("phone", phone);
            data.put("hasBoughtPhone", true);
        } else {
            data.put("phone", this.getHiddenPhone());
            data.put("hasBoughtPhone", false);
        }

        data = JSONService.keepAllowedKeys(data, keys);
        return data;
    }
}
