package KBD.models;

/**
 * Created by sadegh on 2/12/18.
 */
public abstract class User {
    static private int idCounter;

    static {
        idCounter = 1;
    }

    private int id;
    private String name;

    public User(String name) {
        this.id = (idCounter++);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}