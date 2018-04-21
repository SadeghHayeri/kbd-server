package KBD.models;

import KBD.Config;
import KBD.Database;
import KBD.v1.services.JSONService;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by sadegh on 2/12/18.
 */
public class IndividualUser extends User {
    private final static String TABLE_NAME = "individual_users";
    private String username;
    private String phone;
    private int balance;
    private String password;
    private ArrayList<House> paidHouseQueue;

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
                            TABLE_NAME, name, phone, balance, username, password
                    )
            );
        else if(!isModified)
            executeUpdate(
                    String.format(
                            "UPDATE %s name = '%s', phone = '%s', balance = '%d', username = '%s', password = '%s' WHERE id = %d",
                            TABLE_NAME, name, phone, balance, username, password, id
                    )
            );
    }

    public void addBalance(int balanceValue) {
        assert balanceValue >= 0;
        balance += balanceValue;
        isModified = true;
    }

    public boolean hasPaid(House house) {
        return false;
        //TODO: query
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

//        TODO: move it to save method!
//        executeUpdate(
//                String.format("INSERT INTO %s VALUES (%d, '%s', %d)", TABLE_NAME, id, house.getId(), house.getOwner())
//        );

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
