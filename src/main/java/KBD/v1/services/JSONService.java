package KBD.v1.services;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

public class JSONService {
    static public JSONObject keepAllowedKeys(JSONObject data, List<String> keys) {
        JSONObject finalData = new JSONObject();
        for (Iterator<String> it = data.keys(); it.hasNext(); ) {
            String key = it.next();
            if (keys.contains(key))
                finalData.put(key, data.get(key));
        }
        return finalData;
    }
}
