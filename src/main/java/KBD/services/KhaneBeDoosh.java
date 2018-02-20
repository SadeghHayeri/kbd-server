package KBD.services;


import KBD.models.House;
import KBD.models.Logger;
import KBD.models.RealStateUser;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

import java.util.ArrayList;

public class KhaneBeDoosh extends HouseFetcher {

    protected KhaneBeDoosh(RealStateUser realStateUser) {
        super(realStateUser);
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
