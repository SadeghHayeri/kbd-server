package KBD.models;

import KBD.Config;
import KBD.Database;
import KBD.v1.services.JSONService;
import KBD.v1.services.SecurityService;
import org.json.JSONObject;

import java.sql.*;
import java.util.Arrays;
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
    private boolean isAdmin;
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
                        resultSet.getString("password"),
                        resultSet.getBoolean("is_admin")
                );
            }

            connection.close();
            return individualUser;
        } catch (SQLException e) {
            Logger.error(e.getMessage());
            return null;
        }
    }

    private IndividualUser(int id, String name, String username, String phone, int balance, String password, boolean isAdmin) {
        super(id, name);
        this.username = username;
        this.phone = phone;
        this.balance = balance;
        this.password = password;
        this.paidHouseQueue = new ArrayList<>();
        this.isAdmin = isAdmin;

        this.isSaved = true;
    }

    public IndividualUser(String name, String username, String phone, int balance, String password, boolean isAdmin) {
        super(name);
        this.username = username;
        this.phone = phone;
        this.balance = balance;
        this.password = SecurityService.MD5(password);
        this.paidHouseQueue = new ArrayList<>();
        this.isAdmin = isAdmin;
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
                        resultSet.getString("password"),
                        resultSet.getBoolean("is_admin")
                );
            }

            connection.close();
        } catch (SQLException e) {
            Logger.error(e.getMessage());
        }
        return user;
    }

    public void save() {
        if(!isSaved)
            executeUpdate(
                    String.format(
                            "INSERT INTO %s (name, phone, balance, username, password, is_admin) VALUES (?, ?, %d, ?, ?, %d)",
                            Database.INDIVIDUAL_USERS_TB, balance, (isAdmin ? 1 : 0)
                    ),
                    Arrays.asList(name, phone, username, password)
            );
        else if(isModified)
            executeUpdate(
                    String.format(
                            "UPDATE %s SET name = ?, phone = ?, balance = %d, username = ?, password = ? WHERE id = %d",
                            Database.INDIVIDUAL_USERS_TB, balance, id
                    ),
                    Arrays.asList(name, phone, username, password)
            );
        for (House house : paidHouseQueue)
            executeUpdate(
                    String.format(
                            "INSERT INTO %s (user_id, house_id, house_owner) VALUES (%d, ?, %d)",
                            Database.PAID_HOUSES_TB, id, house.getOwner()
                    ),
                    Arrays.asList(house.getId())
            );
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
        data.put("isAdmin", isAdmin);

        data = JSONService.keepAllowedKeys(data, keys);
        return data;
    }
}
