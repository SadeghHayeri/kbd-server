package KBD.models;

/**
 * Created by sadegh on 2/12/18.
 */
public class IndividualUser extends User {
    private String username;
    private String phone;
    private float balance;
    private String password;

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
}
