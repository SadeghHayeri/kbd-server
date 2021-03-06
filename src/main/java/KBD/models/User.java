package KBD.models;

import KBD.v1.services.JSONService;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by sadegh on 2/12/18.
 */
public abstract class User extends BaseModel{
    protected int id;
    protected String name;

    public User(String name) {
        this.isSaved = false;
        this.id = BaseModel.NOT_INITIATE;
        this.name = name;
    }

    protected User(int id, String name) {
        this.id = id;
        this.name = name;

        this.isSaved = true;
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