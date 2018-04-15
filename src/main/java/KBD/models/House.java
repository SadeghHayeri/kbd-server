package KBD.models;

import KBD.Config;
import KBD.Database;
import KBD.models.enums.BuildingType;
import KBD.models.enums.DealType;
import KBD.models.enums.HouseOwner;
import KBD.v1.services.JSONService;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by sadegh on 2/12/18.
 */
public class House {
    public static final String TABLE_NAME = "houses";
    static private int idCounter;

    static {
        idCounter = 1;
    }
    
    public static void createSellHouse(int owner, BuildingType buildingType, int area, String address, String imageURL,
                                       int sellPrice, String phone, String description) {
        try {
            Connection connection = Database.getConnection();

            Statement statement = connection.createStatement();
            statement.executeUpdate( String.format(
                    "INSERT INTO %s (owner, building_type, area, address, image_URL, deal_type, sell_price, phone, description) " +
                            "VALUES (%d, %d, %d, '%s', '%s', %d, %d, '%s', '%s')",
                    TABLE_NAME, owner, buildingType.toInteger(), area, address, imageURL, DealType.BUY.toInteger(), sellPrice, phone, description)
            );

            connection.close();
        } catch (SQLException e) {
            Logger.error(e.getMessage());
        }
    }

    public static void createRentHouse(int owner, BuildingType buildingType, int area, String address, String imageURL,
                                       int basePrice, int rentPrice, String phone, String description) {
        try {
            Connection connection = Database.getConnection();

            Statement statement = connection.createStatement();
            statement.executeUpdate( String.format(
                    "INSERT INTO %s (owner, building_type, area, address, image_URL, deal_type, base_price, rent_price, phone, description) " +
                            "VALUES (%d, %d, %d, '%s', '%s', %d, %d, %d, '%s', '%s')",
                    TABLE_NAME, owner, buildingType.toInteger(), area, address, imageURL, DealType.RENTAL.toInteger(), basePrice, rentPrice, phone, description)
            );

            connection.close();
        } catch (SQLException e) {
            Logger.error(e.getMessage());
        }
    }


    public void save() {
        if (dealType == DealType.RENTAL)
            createRentHouse(owner, buildingType, area, address, imageURL, basePrice, rentPrice, phone, description);
        else
            createSellHouse(owner, buildingType, area, address, imageURL, sellPrice, phone, description);
    }

    private String id;
    private BuildingType buildingType;
    private int area;
    private String address;
    private String imageURL;
    private DealType dealType;
    private int basePrice;
    private int rentPrice;
    private int sellPrice;
    private String phone;
    private String description;
    private int owner;

    public House(BuildingType buildingType, int area, String address, int sellPrice, String phone, String description) {
        this.dealType = DealType.BUY;

//        this.owner = HouseOwner.SYSTEM;
        this.id = Integer.toString(idCounter++);
        this.buildingType = buildingType;
        this.area = area;
        this.address = address;
        this.sellPrice = sellPrice;
        this.phone = phone;
        this.description = description;
        this.imageURL = Config.NO_IMAGE_PATH;
    }

    public House(int owner, String id, BuildingType buildingType, int area, String address, int sellPrice, String phone, String description, String imageURL) {
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

    public House(BuildingType buildingType, int area, String address, int basePrice, int rentPrice, String phone, String description) {
        this.dealType = DealType.RENTAL;

//        this.owner = HouseOwner.SYSTEM;
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

    public House(int owner, String id, BuildingType buildingType, int area, String address, int basePrice, int rentPrice, String phone, String description, String imageURL) {
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

    public int getBasePrice() {
        return basePrice;
    }

    public int getRentPrice() {
        return rentPrice;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public String getPhone() {
        return phone;
    }

    public String getHiddenPhone() {
        return phone.substring(0, 2) + "*****" + phone.substring(7);
    }

    public String getDescription() {
        return description;
    }

    public int getOwner() {
        return owner;
    }

    public JSONObject toJson (List<String> keys) {
        JSONObject data = new JSONObject();
        data.put("id", id);
//        data.put("owner", owner.toString());
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
