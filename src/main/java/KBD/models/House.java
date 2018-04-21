package KBD.models;

import KBD.Config;
import KBD.Database;
import KBD.models.enums.BuildingType;
import KBD.models.enums.DealType;
import KBD.models.enums.HouseOwner;
import KBD.v1.services.JSONService;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sadegh on 2/12/18.
 */
public class House extends BaseModel{
    public static final String TABLE_NAME = "houses";

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

    public int getOwner() {
        return owner;
    }

    private int owner;

    public void save() {
        if(!isSaved) {
            if (dealType == DealType.RENTAL) {
                executeUpdate(
                        String.format(
                                "INSERT INTO %s (id, owner, building_type, area, address, image_URL, deal_type, base_price, rent_price, phone, description) " +
                                        "VALUES ('%s', %d, %d, %d, '%s', '%s', %d, %d, %d, '%s', '%s')",
                                TABLE_NAME, id, owner, buildingType.toInteger(), area, address, imageURL, DealType.RENTAL.toInteger(), basePrice, rentPrice, phone, description
                        )
                );
            } else {
                executeUpdate(
                        String.format(
                                "INSERT INTO %s (id, owner, building_type, area, address, image_URL, deal_type, sell_price, phone, description) " +
                                        "VALUES ('%s', %d, %d, %d, '%s', '%s', %d, %d, '%s', '%s')",
                                TABLE_NAME, id, owner, buildingType.toInteger(), area, address, imageURL, DealType.BUY.toInteger(), sellPrice, phone, description
                        )
                );
            }
        } else if(!isModified) {
            if (dealType == DealType.RENTAL) {
                executeUpdate(
                        String.format(
                                "UPDATE %s owner = '%s', building_type = %d, area = '%s', address = '%s', image_URL = '%s', deal_type = %d, base_price = %d, rent_price = %d, phone = '%s', description = '%s' WHERE id = '%s'",
                                TABLE_NAME, owner, buildingType.toInteger(), area, address, imageURL, DealType.RENTAL.toInteger(), basePrice, rentPrice, phone, description, id
                        )
                );
            } else {
                executeUpdate(
                        String.format(
                                "UPDATE %s owner = '%s', building_type = %d, area = %d, address = '%s', image_URL = '%s', deal_type = %d, sell_price = %d, phone = '%s', description = '%s' WHERE id = '%s'",
                                TABLE_NAME, owner, buildingType.toInteger(), area, address, imageURL, DealType.BUY.toInteger(), sellPrice, phone, description, id
                        )
                );
            }
        }
    }

    public static House find(HouseOwner houseOwner, String houseId) {
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(
                    String.format(
                            "SELECT * FROM %s WHERE id = '%s' and owner = '%s'",
                            TABLE_NAME, RealStateUser.find(houseOwner.toString()).getId(), houseId
                    )
            );

            House house = null;
            if (resultSet.next())
                house = make(resultSet);
            connection.close();
            return house;
        } catch (SQLException e) {
            Logger.error(e.getMessage());
            return null;
        }
    }

    public static ArrayList<House> filter(int minimumArea, BuildingType buildingType, DealType dealType, int maximumPrice) {
        ArrayList<House> houses = new ArrayList<>();
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            String price = "sell_price";
            if (dealType == DealType.RENTAL)
                price = "base_price";

            ResultSet resultSet = statement.executeQuery(
                    String.format(
                            "SELECT * FROM %s WHERE area >= %d and %s <= %d and deal_type = %d and building_type = %d",
                            TABLE_NAME, minimumArea, price, maximumPrice, dealType.toInteger(), buildingType.toInteger()
                    )
            );

            while (resultSet.next()) {
                houses.add(make(resultSet));
            }
            connection.close();
        } catch (SQLException e) {
            Logger.error(e.getMessage());
        }
        return houses;
    }

    private static House make(ResultSet resultSet) {
        House house = null;
        try {
            if (DealType.parseDealType(resultSet.getInt("deal_type")) == DealType.BUY)
                house = new House(
                        resultSet.getInt("owner"),
                        resultSet.getString("id"),
                        BuildingType.parseBuildingType(resultSet.getInt("building_type")),
                        resultSet.getInt("area"),
                        resultSet.getString("address"),
                        resultSet.getInt("sell_price"),
                        resultSet.getString("phone"),
                        resultSet.getString("description"),
                        resultSet.getString("image_URL")
                );
            else
                house = new House(
                        resultSet.getInt("owner"),
                        resultSet.getString("id"),
                        BuildingType.parseBuildingType(resultSet.getInt("building_type")),
                        resultSet.getInt("area"),
                        resultSet.getString("address"),
                        resultSet.getInt("base_price"),
                        resultSet.getInt("rent_price"),
                        resultSet.getString("phone"),
                        resultSet.getString("description"),
                        resultSet.getString("image_URL")
                );
        } catch (SQLException e) {
            Logger.error(e.getMessage());
        }
        return house;
    }

    public House(BuildingType buildingType, int area, String address, int sellPrice, String phone, String description) {
        this.dealType = DealType.BUY;

        this.owner = RealStateUser.find(HouseOwner.SYSTEM.toString()).getId();
        this.id = UUID.randomUUID().toString();
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

        this.isSaved = true;
    }

    public House(BuildingType buildingType, int area, String address, int basePrice, int rentPrice, String phone, String description) {
        this.dealType = DealType.RENTAL;

        this.owner = RealStateUser.find(HouseOwner.SYSTEM.toString()).getId();
        this.id = UUID.randomUUID().toString();
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

        this.isSaved = true;
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

    public DealType getDealType() {
        return dealType;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public String getHiddenPhone() {
        return phone.substring(0, 2) + "*****" + phone.substring(7);
    }

    public JSONObject toJson (List<String> keys) {
        JSONObject data = new JSONObject();
        data.put("id", id);
        data.put("owner", RealStateUser.find(owner).getName());
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
