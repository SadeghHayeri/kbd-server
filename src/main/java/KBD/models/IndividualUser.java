package KBD.models;

import KBD.Config;
import KBD.v1.services.JSONService;
import org.json.JSONObject;

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
    private ArrayList<PaidHouse> paidHouses;

    public IndividualUser(String name, String username, String phone, int balance, String password) {
        super(name);
        this.username = username;
        this.phone = phone;
        this.balance = balance;
        this.password = password;
        this.paidHouses = new ArrayList<>();
    }

    public int getBalance() {
        return balance;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public void addBalance(int balanceValue) {
        balance += balanceValue;
    }

    public boolean hasPaid(House house) {
        for (PaidHouse paidHouse : paidHouses)
            if(paidHouse.houseOwner.equals(house.getOwner()))
                if(paidHouse.houseId.equals(house.getId()))
                    return true;
        return false;
    }

    public boolean pay(House house) {
        if(hasPaid(house))
            return false;

        if(balance < Config.HOUSE_OWNER_NUMBER_PRICE)
            return false;

        balance -= Config.HOUSE_OWNER_NUMBER_PRICE;
        paidHouses.add(new PaidHouse(house.getOwner(), house.getId()));

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
