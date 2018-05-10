package KBD.models;

import KBD.Config;
import KBD.Database;
import KBD.models.enums.BuildingType;
import KBD.models.enums.DealType;
import KBD.models.enums.HouseOwner;
import KBD.v1.services.JSONService;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sadegh on 2/12/18.
 */
public class House extends BaseModel{
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
        try {
            Connection connection = Database.getConnection();
            if(!isSaved) {
                if (dealType == DealType.RENTAL) {
                    String SQL = String.format("INSERT INTO %s (id, owner, building_type, area, address, image_URL, deal_type, base_price, rent_price, phone, description) \" +\n" +
                            "                                            \"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Database.HOUSES_TB);
                    PreparedStatement pstmt = connection.prepareStatement(SQL);
                    pstmt.setString(1, id);
                    pstmt.setString(2, String.valueOf(owner));
                    pstmt.setString(3, String.valueOf(buildingType.toInteger()));
                    pstmt.setString(4, String.valueOf(area));
                    pstmt.setString(5, address);
                    pstmt.setString(6, imageURL);
                    pstmt.setString(7, String.valueOf(DealType.RENTAL.toInteger()));
                    pstmt.setString(8, String.valueOf(basePrice));
                    pstmt.setString(9, String.valueOf(rentPrice));
                    pstmt.setString(10, phone);
                    pstmt.setString(11, description);
                    pstmt.executeQuery();
                } else {
                    String SQL = String.format("INSERT INTO %s (id, owner, building_type, area, address, image_URL, deal_type, sell_price, phone, description) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Database.HOUSES_TB);
                    PreparedStatement pstmt = connection.prepareStatement(SQL);
                    pstmt.setString(1, id);
                    pstmt.setString(2, String.valueOf(owner));
                    pstmt.setString(3, String.valueOf(buildingType.toInteger()));
                    pstmt.setString(4, String.valueOf(area));
                    pstmt.setString(5, address);
                    pstmt.setString(6, imageURL);
                    pstmt.setString(7, String.valueOf(DealType.BUY.toInteger()));
                    pstmt.setString(8, String.valueOf(sellPrice));
                    pstmt.setString(9, phone);
                    pstmt.setString(10, description);
                    pstmt.executeQuery();
                }
            } else if(isModified) {
                if (dealType == DealType.RENTAL) {
                    String SQL = String.format("UPDATE %s owner = ?, building_type = ?, area = ?, address = ?, image_URL = ?, deal_type = ?, base_price = ?, rent_price = ?, phone = ?, description = ? WHERE id = ?", Database.HOUSES_TB);
                    PreparedStatement pstmt = connection.prepareStatement(SQL);
                    pstmt.setString(1, String.valueOf(owner));
                    pstmt.setString(2, String.valueOf(buildingType.toInteger()));
                    pstmt.setString(3, String.valueOf(area));
                    pstmt.setString(4, address);
                    pstmt.setString(5, imageURL);
                    pstmt.setString(6, String.valueOf(DealType.RENTAL.toInteger()));
                    pstmt.setString(7, String.valueOf(basePrice));
                    pstmt.setString(8, String.valueOf(rentPrice));
                    pstmt.setString(9, phone);
                    pstmt.setString(10, description);
                    pstmt.executeQuery();
                } else {
                    String SQL = String.format("UPDATE %s owner = ?, building_type = ?, area = ?, address = ?, image_URL = ?, deal_type = ?, sell_price = ?, phone = ?, description = ? WHERE id = ?", Database.HOUSES_TB);
                    PreparedStatement pstmt = connection.prepareStatement(SQL);
                    pstmt.setString(1, String.valueOf(owner));
                    pstmt.setString(2, String.valueOf(buildingType.toInteger()));
                    pstmt.setString(3, String.valueOf(area));
                    pstmt.setString(4, address);
                    pstmt.setString(5, imageURL);
                    pstmt.setString(6, String.valueOf(DealType.BUY.toInteger()));
                    pstmt.setString(7, String.valueOf(sellPrice));
                    pstmt.setString(9, phone);
                    pstmt.setString(10, description);
                    pstmt.setString(11, id);
                    pstmt.executeQuery();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static House find(HouseOwner houseOwner, String houseId) {
        try {
            Connection connection = Database.getConnection();

            String SQL = String.format("SELECT * FROM %s WHERE owner = ? and id = ?", Database.PAID_HOUSES_TB);
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setString(1, String.valueOf(RealStateUser.find(houseOwner.toString()).getId()));
            pstmt.setString(2, houseId);
            ResultSet resultSet = pstmt.executeQuery();

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

            String SQL = String.format("SELECT * FROM %s WHERE area >= ? and ? <= ? and deal_type = ? and building_type = ?", Database.HOUSES_TB);
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setString(1, String.valueOf(minimumArea));
            pstmt.setString(2, price);
            pstmt.setString(3, String.valueOf(maximumPrice));
            pstmt.setString(4, String.valueOf(dealType.toInteger()));
            pstmt.setString(5, String.valueOf(buildingType.toInteger()));
            ResultSet resultSet = pstmt.executeQuery();

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
                        resultSet.getString("image_URL"),
                        true
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
                        resultSet.getString("image_URL"),
                        true
                );
        } catch (SQLException e) {
            Logger.error(e.getMessage());
        }
        return house;
    }

    public House(BuildingType buildingType, int area, String address, int sellPrice, String phone, String description) {
        this.dealType = DealType.BUY;

        this.owner = Config.SYSTEM_USER_ID;
        this.id = UUID.randomUUID().toString();
        this.buildingType = buildingType;
        this.area = area;
        this.address = address;
        this.sellPrice = sellPrice;
        this.phone = phone;
        this.description = description;
        this.imageURL = Config.NO_IMAGE_PATH;
    }

    public House(int owner, String id, BuildingType buildingType, int area, String address, int sellPrice, String phone, String description, String imageURL, boolean isSaved) {
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

        this.isSaved = isSaved;
    }

    public House(BuildingType buildingType, int area, String address, int basePrice, int rentPrice, String phone, String description) {
        this.dealType = DealType.RENTAL;

        this.owner = Config.SYSTEM_USER_ID;
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

    public House(int owner, String id, BuildingType buildingType, int area, String address, int basePrice, int rentPrice, String phone, String description, String imageURL, boolean isSaved) {
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

        this.isSaved = isSaved;
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

        if (user != null && user.hasPaid(this)) {
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
