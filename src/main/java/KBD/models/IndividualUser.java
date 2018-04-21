package KBD.models;

import KBD.Config;
import KBD.Database;
import KBD.models.enums.DealType;
import KBD.v1.services.JSONService;
import org.json.JSONObject;
import org.omg.CORBA.DATA_CONVERSION;

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
                            Database.PAID_HOUSES_TB, id
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
        this.password = password;
        this.paidHouseQueue = new ArrayList<>();
    }

    public void save() {
        if(!isSaved)
            executeUpdate(
                    String.format(
                            "INSERT INTO %s (name, phone, balance, username, password) VALUES ('%s', '%s', %d, '%s', '%s')",
                            Database.INDIVIDUAL_USERS_TB, name, phone, balance, username, password
                    )
            );
        else if(!isModified)
            executeUpdate(
                    String.format(
                            "UPDATE %s name = '%s', phone = '%s', balance = '%d', username = '%s', password = '%s' WHERE id = %d",
                            Database.INDIVIDUAL_USERS_TB, name, phone, balance, username, password, id
                    )
            );

        for (House house : paidHouseQueue) {
            executeUpdate(
                String.format(
                        "INSERT INTO %s (user_id, house_id, house_owner) VALUES ('%d', '%s', %d)",
                        Database.PAID_HOUSES_TB, id, house.getId(), house.getOwner(), username, password
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
                            "SELECT * FROM %s WHERE user_id = %d, house_id = '%s', house_owner = %d",
                            Database.PAID_HOUSES_TB, id, house.getId(), house.getOwner()
                    )
            );

            paid = resultSet != null;
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
        isModified = true;

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
