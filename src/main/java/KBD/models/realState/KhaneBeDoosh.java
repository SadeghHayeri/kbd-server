package KBD.models.realState;

import KBD.models.House;
import KBD.models.Logger;
import KBD.models.RealStateUser;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

import java.util.ArrayList;

public class KhaneBeDoosh extends RealStateUser {
    public KhaneBeDoosh(String name, String apiAddress) {
        super(name, apiAddress);
    }

    @Override
    protected ArrayList<House> parseGetHouseListResponse(HttpResponse response) {
        JSONObject jsonObj = new JSONObject(response.getEntity());

        Logger.info(jsonObj.getString("status"));

        return null;
    }

    @Override
    protected House parseGetHouseResponse(HttpResponse response) {
        JSONObject jsonObj = new JSONObject(response.getEntity());

        Logger.info(jsonObj.getString("status"));

        return null;
    }

    @Override
    protected String getContentType() {
        return "application/json";
    }
}
