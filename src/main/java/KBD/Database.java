package KBD;

import KBD.models.Logger;
import KBD.v1.Exceptions.NotFoundException;
import KBD.models.House;
import KBD.models.IndividualUser;
import KBD.models.RealStateUser;
import KBD.models.enums.BuildingType;
import KBD.models.enums.DealType;
import KBD.models.enums.HouseOwner;
import KBD.models.realState.KhaneBeDoosh;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {
    public static ArrayList<House> houses = new ArrayList<>();
    private static ArrayList<IndividualUser> users = new ArrayList<>();
    private static ArrayList<RealStateUser> realStateUsers = new ArrayList<>();
    private static BasicDataSource source = new org.apache.commons.dbcp.BasicDataSource();

    static {
        source.setDriverClassName("org.sqlite.JDBC");
        source.setUrl("jdbc:sqlite:database.db");

        createTables();
        seed();
        fetchHouses();
    }

    public static Connection getConnection() throws SQLException {
        return source.getConnection();
    }

    private static void seed() {
        users.add(new IndividualUser("بهنام همایون", "behnam_homayoon", "09123456789", 0, "123"));
        realStateUsers.add(new KhaneBeDoosh("khane-be-doosh", "http://acm.ut.ac.ir/khaneBeDoosh/v2"));

        houses.add(new House(BuildingType.VILLA, 100, "be to che", 1500, "09333564931", "nadare"));
        houses.add(new House(BuildingType.APARTMENT, 102, "address", 100, "09333564932", "nadare"));
        houses.add(new House(BuildingType.APARTMENT, 1000, "khoone", 2500, "09333564933", "nadare"));
        houses.add(new House(BuildingType.VILLA, 50, "salam", 1550, "09333564934", "nadare"));

        houses.add(new House(BuildingType.VILLA, 100, "be to che", 1500, 100, "09333564931", "nadare"));
        houses.add(new House(BuildingType.APARTMENT, 102, "address", 100, 500, "09333564932", "nadare"));
        houses.add(new House(BuildingType.APARTMENT, 1000, "khoone", 2500, 200, "09333564933", "nadare"));
        houses.add(new House(BuildingType.VILLA, 50, "salam", 1550, 150, "09333564934", "nadare"));

        for (House house: houses)
            house.save();

        for (IndividualUser user: users)
            user.save();

        for (RealStateUser realStateUser: realStateUsers)
            realStateUser.save();
    }

    private static void createTables() {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS individual_users(" +
                            "id INTEGER PRIMARY KEY," +
                            "name VARCHAR(255)," +
                            "phone VARCHAR(255)," +
                            "balance INTEGER," +
                            "username VARCHAR(255)," +
                            "password VARCHAR(255)" +
                            ")");

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS realstate_users(" +
                            "id INTEGER PRIMARY KEY," +
                            "name VARCHAR(255)," +
                            "api_address VARCHAR(255)" +
                            ")");

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS houses(" +
                            "id VARCHAR(255)," +
                            "owner INTEGER," +
                            "building_type INTEGER," +
                            "area INTEGER," +
                            "address VARCHAR(255)," +
                            "image_URL VARCHAR(255)," +
                            "deal_type INTEGER," +
                            "base_price INTEGER," +
                            "rent_price INTEGER," +
                            "sell_price INTEGER," +
                            "phone VARCHAR(255)," +
                            "description TEXT," +
                            "PRIMARY KEY (id, owner)," +
                            "FOREIGN KEY (owner) REFERENCES realstate_users(id) ON DELETE CASCADE" +
                            ")");

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS paid_houses(" +
                            "user_id INTEGER," +
                            "house_id VARCHAR(255)," +
                            "house_owner INTEGER," +
                            "PRIMARY KEY (user_id, house_id, house_owner)" +
                            "FOREIGN KEY (user_id) REFERENCES individual_users(id) ON DELETE CASCADE," +
                            "FOREIGN KEY (house_id, house_owner) REFERENCES houses(id, owner) ON DELETE CASCADE," +
                            "FOREIGN KEY (house_owner) REFERENCES realstate_users(id) ON DELETE CASCADE" +
                            ")");

            Logger.info("transactions done!");
            connection.close();
        } catch (SQLException e) {
            Logger.error(e.getMessage());
        }
    }

    private static void fetchHouses() {
        for (RealStateUser realStateUser: RealStateUser.list()) {
            realStateUser.deleteHouses();
            for (House house: realStateUser.getHouses()) {
                house.save();
            }
        }
    }

    public static void addHouse(House newHouse) {
        houses.add(newHouse);
    }

    public static IndividualUser getUser(int index) {
        return users.get(index);
    }

    public static ArrayList<House> getHouses() {
        ArrayList<House> houses = getOwnHouses();
        houses.addAll(getRealStateHouses());

        return houses;
    }

    public static ArrayList<House> getHouses(int minimumArea, BuildingType buildingType, DealType dealType, float maximumPrice) {
        ArrayList<House> houses = getOwnHouses(minimumArea, buildingType, dealType, maximumPrice);
        houses.addAll(getRealStateHouses(minimumArea, buildingType, dealType, maximumPrice));

        return houses;
    }

    private static House getOwnHouse(String id) throws NotFoundException {
        for (House house : houses)
            if(house.getId().equals(id))
                return house;
        throw new NotFoundException("House with id " + id + " not found!");
    }

    public static ArrayList<House> getOwnHouses() {
        return houses;
    }

    public static ArrayList<House> getOwnHouses(int minimumArea, BuildingType buildingType, DealType dealType, float maximumPrice) {
        return sampleFilter(houses, minimumArea, buildingType, dealType, maximumPrice);
    }

    private static ArrayList<House> getRealStateHouses() {
        ArrayList<House> allHouses = new ArrayList<>();

        for (RealStateUser realStateUser : realStateUsers)
            allHouses.addAll(realStateUser.getHouses());

        return allHouses;
    }

    private static ArrayList<House> getRealStateHouses(int minimumArea, BuildingType buildingType, DealType dealType, float maximumPrice) {
        ArrayList<House> allHouses = getRealStateHouses();
        return sampleFilter(allHouses, minimumArea, buildingType, dealType, maximumPrice);
    }

    private static ArrayList<House> sampleFilter(ArrayList<House> data, int minimumArea, BuildingType buildingType, DealType dealType, float maximumPrice) {
        ArrayList<House> result = new ArrayList<>();

        for (House house : data) {
            if (house.getBuildingType() == buildingType) {
                if (house.getArea() >= minimumArea) {
                    if (house.getDealType() == dealType) {
                        if (house.getSellPrice() <= maximumPrice)
                            result.add(house);
                    }
                }
            }
        }

        return result;
    }

    public static House getHouse(HouseOwner houseOwner, String houseId) throws NotFoundException {
        if(houseOwner == HouseOwner.SYSTEM) {
            return House.find(HouseOwner.SYSTEM, houseId);
        } else {
            RealStateUser realStateUser = RealStateUser.find(houseOwner.toString());
            House house = realStateUser.getHouse(houseId);
            if (house != null)
                return house;
        }
        throw new NotFoundException("House with id " + houseId + " not found!");
    }
}
