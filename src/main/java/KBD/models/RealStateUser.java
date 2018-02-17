package KBD.models;


/**
 * Created by sadegh on 2/12/18.
 */
public class RealStateUser extends User {
    private String apiAddress;

    public RealStateUser(String name, String apiAddress) {
        super(name);
        this.apiAddress = apiAddress;
    }
}
