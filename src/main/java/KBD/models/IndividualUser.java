package KBD.models;

import KBD.Config;
import KBD.Database;
import KBD.v1.services.JSONService;
import KBD.v1.services.SecurityService;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
            Statement statement = connection.createStatement();

            String hashedPassword = SecurityService.MD5(password);
            ResultSet resultSet = statement.executeQuery(
                    String.format(
                            "SELECT * FROM %s WHERE username = '%s' and password = '%s'",
                            Database.INDIVIDUAL_USERS_TB, username, hashedPassword
                    )
            );

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
        if(!isSaved)
            executeUpdate(
                    String.format(
                            "INSERT INTO %s (name, phone, balance, username, password) VALUES ('%s', '%s', %d, '%s', '%s')",
                            Database.INDIVIDUAL_USERS_TB, name, phone, balance, username, password
                    )
            );
        else if(isModified)
            executeUpdate(
                    String.format(
                            "UPDATE %s SET name = '%s', phone = '%s', balance = %d, username = '%s', password = '%s' WHERE id = %d",
                            Database.INDIVIDUAL_USERS_TB, name, phone, balance, username, password, id
                    )
            );

        for (House house : paidHouseQueue) {
            executeUpdate(
                String.format(
                        "INSERT INTO %s (user_id, house_id, house_owner) VALUES (%d, '%s', %d)",
                        Database.PAID_HOUSES_TB, id, house.getId(), house.getOwner()
                )
            );
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
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(
                    String.format(
                            "SELECT * FROM %s WHERE user_id = %d and house_id = '%s' and house_owner = %d",
                            Database.PAID_HOUSES_TB, id, house.getId(), house.getOwner()
                    )
            );

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
