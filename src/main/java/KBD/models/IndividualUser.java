package KBD.models;

import KBD.Config;

import java.util.ArrayList;

/**
 * Created by sadegh on 2/12/18.
 */
public class IndividualUser extends User {
    private String username;
    private String phone;
    private float balance;
    private String password;
    private ArrayList<PaidHouse> paidHouses;

    public IndividualUser(String name, String username, String phone, float balance, String password) {
        super(name);
        this.username = username;
        this.phone = phone;
        this.balance = balance;
        this.password = password;
    }

    public float getBalance() {
        return balance;
    }

    public void addBalance(float balanceValue) {
        balance += balanceValue;
    }

    public boolean hasPaid(House house) {
        for (PaidHouse paidHouse : paidHouses)
            if(paidHouse.houseOwner.equals(house.getOwner()))
                if(paidHouse.houseId.equals(house.getId()))
                    return true;
        return false;
    }

    public boolean pay(House house) {   //TODO: use exception!
        if(balance >= Config.HOUSE_OWNER_NUMBER_PRICE)
            return false;

        if(hasPaid(house))
            return false;

        balance -= Config.HOUSE_OWNER_NUMBER_PRICE;
        paidHouses.add(new PaidHouse(house.getOwner(), house.getId()));
        return true;
    }

}
