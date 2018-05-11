package KBD.models;

import KBD.Config;
import KBD.Database;
import KBD.v1.services.JSONService;
import KBD.v1.services.SecurityService;
import org.json.JSONObject;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by sadegh on 2/12/18.
 */
public class IndividualUser extends User {
    private String username;
    private String phone;
    private int balance;
    private String password;
    private ArrayList<House> paidHouseQueue;

    public static IndividualUser find(int id) {
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            IndividualUser individualUser = null;

            ResultSet resultSet = statement.executeQuery(
                    String.format(
                            "SELECT * FROM %s WHERE id = %d",
                            Database.INDIVIDUAL_USERS_TB, id
                    )
            );

            if (resultSet != null && resultSet.next()) {
                individualUser = new IndividualUser(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("username"),
                        resultSet.getString("phone"),
                        resultSet.getInt("balance"),
                        resultSet.getString("password")
                );
            }

            connection.close();
            return individualUser;
        } catch (SQLException e) {
            Logger.error(e.getMessage());
            return null;
        }
    }

    private IndividualUser(int id, String name, String username, String phone, int balance, String password) {
        super(id, name);
        this.username = username;
        this.phone = phone;
        this.balance = balance;
        this.password = password;
        this.paidHouseQueue = new ArrayList<>();

        this.isSaved = true;
    }

    public IndividualUser(String name, String username, String phone, int balance, String password) {
        super(name);
        this.username = username;
        this.phone = phone;
        this.balance = balance;
        this.password = SecurityService.MD5(password);
        this.paidHouseQueue = new ArrayList<>();
    }

    public static IndividualUser find(String username, String password) {
        IndividualUser user = null;
        try {
            Connection connection = Database.getConnection();

            String SQL = String.format("SELECT * FROM %s WHERE username = ? and password = ?", Database.INDIVIDUAL_USERS_TB);
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setString(1, username);
            pstmt.setString(2, SecurityService.MD5(password));
            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next()) {
                user = new IndividualUser(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("username"),
                        resultSet.getString("phone"),
                        resultSet.getInt("balance"),
                        resultSet.getString("password")
                );
            }

            connection.close();
        } catch (SQLException e) {
            Logger.error(e.getMessage());
        }
        return user;
    }

    public void save() {
        try {
            Connection connection = Database.getConnection();
            if(!isSaved) {
                String SQL = String.format("INSERT INTO %s (name, phone, balance, username, password) VALUES (?, ?, ?, ?, ?)", Database.INDIVIDUAL_USERS_TB);
                PreparedStatement pstmt = connection.prepareStatement(SQL);
                pstmt.setString(1, name);
                pstmt.setString(2, phone);
                pstmt.setInt(3, balance);
                pstmt.setString(4, username);
                pstmt.setString(5, password);
                pstmt.executeUpdate();
            }
            else if(isModified) {
                String SQL = String.format("UPDATE %s SET name = ?, phone = ?, balance = ?, username = ?, password = ? WHERE id = ?", Database.INDIVIDUAL_USERS_TB);
                PreparedStatement pstmt = connection.prepareStatement(SQL);
                pstmt.setString(1, name);
                pstmt.setString(2, phone);
                pstmt.setInt(3, balance);
                pstmt.setString(4, username);
                pstmt.setString(5, password);
                pstmt.setInt(6, id);
                pstmt.executeUpdate();
            }

            for (House house : paidHouseQueue) {
                String SQL = String.format("INSERT INTO %s (user_id, house_id, house_owner) VALUES (?, ?, ?)", Database.PAID_HOUSES_TB);
                PreparedStatement pstmt = connection.prepareStatement(SQL);
                pstmt.setInt(1, id);
                pstmt.setString(2, house.getId());
                pstmt.setInt(3, house.getOwner());
                pstmt.executeUpdate();
            }

            connection.close();
        } catch (SQLException e) {
            Logger.error(e.getMessage());
        }
    }

    public void addBalance(int balanceValue) {
        assert balanceValue >= 0;
        balance += balanceValue;
        isModified = true;
    }

    public boolean hasPaid(House house) {
        boolean paid = false;
        try {
            Connection connection = Database.getConnection();

            String SQL = String.format("SELECT * FROM %s WHERE user_id = ? and house_id = ? and house_owner = ?", Database.PAID_HOUSES_TB);
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setInt(1, id);
            pstmt.setString(2, house.getId());
            pstmt.setInt(3, house.getOwner());
            ResultSet resultSet = pstmt.executeQuery();

            paid = resultSet.next();
            connection.close();
        } catch (SQLException e) {
            Logger.error(e.getMessage());
        }
        return paid;
    }

    public boolean pay(House house) {
        if(hasPaid(house))
            return false;

        if(balance < Config.HOUSE_OWNER_NUMBER_PRICE)
            return false;

        balance -= Config.HOUSE_OWNER_NUMBER_PRICE;
        paidHouseQueue.add(house);
        isModified = true;

        return true;
    }

    public JSONObject toJson (List<String> keys) {
        JSONObject data = super.toJson(keys);
        data.put("username", username);
        data.put("phone", phone);
        data.put("balance", balance);

        data = JSONService.keepAllowedKeys(data, keys);
        return data;
    }
}
