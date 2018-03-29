package KBD.models;

import KBD.v1.services.JSONService;
import org.json.JSONObject;

import java.util.List;

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

    public JSONObject toJson (List<String> keys) {
        JSONObject data = new JSONObject();
        data.put("id", id);
        data.put("name", name);

        data = JSONService.keepAllowedKeys(data, keys);
        return data;
    }
}