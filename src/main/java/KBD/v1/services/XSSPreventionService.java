package KBD.v1.services;

import org.json.JSONObject;

import java.util.Iterator;

public class XSSPreventionService {
    private static String secure(String input) {
        input = input.replaceAll("&", "&amp");
        input = input.replaceAll("<", "&lt;");
        input = input.replaceAll(">", "&gt;");
        input = input.replaceAll("'", "&#39");
        input = input.replaceAll("\"", "&quot;");
        return input;
    }

    public static JSONObject secure(JSONObject json) {
        Iterator<?> keys = json.keys();
        while(keys.hasNext()) {
            String key = (String)keys.next();
            if (json.get(key) instanceof String)
                json.put(key, XSSPreventionService.secure((String) json.get(key)));
        }
        return json;
    }
}
