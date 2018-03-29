package KBD.models.realState;

import KBD.models.House;
import KBD.models.RealStateUser;
import KBD.models.enums.BuildingType;
import KBD.models.enums.HouseOwner;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class KhaneBeDoosh extends RealStateUser {
    public KhaneBeDoosh(String name, String apiAddress) {
        super(name, apiAddress);
    }

    @Override
    protected ArrayList<House> parseGetHouseListResponse(HttpResponse response) throws IOException {
        String json = EntityUtils.toString(response.getEntity());
        JSONObject jsonObj = new JSONObject(json);

        ArrayList<House> houses = new ArrayList<>();

        JSONArray data = jsonObj.getJSONArray("data");
        for(Object object:  data){
            JSONObject house = (JSONObject) object;
            int dealType = house.getInt("dealType");
            if (dealType == 0)
                houses.add(
                    new House(
                        HouseOwner.KHANE_BE_DOOSH,
                        house.getString("id"),
                        (house.getString("buildingType").equals("آپارتمان") ? BuildingType.APARTMENT : BuildingType.VILLA),
                        house.getInt("area"),
                        "",
                        house.getJSONObject("price").getFloat("sellPrice"),
                        "",
                        "",
                        house.getString("imageURL")
                    )
                );
            else
                houses.add(
                    new House(
                        HouseOwner.KHANE_BE_DOOSH,
                        house.getString("id"),
                        (house.getString("buildingType").equals("آپارتمان") ? BuildingType.APARTMENT : BuildingType.VILLA),
                        house.getInt("area"),
                        "",
                        house.getJSONObject("price").getFloat("basePrice"),
                        house.getJSONObject("price").getFloat("rentPrice"),
                        "",
                        "",
                        house.getString("imageURL")
                    )
                );
        }

        return houses;
    }

    @Override
    protected House parseGetHouseResponse(HttpResponse response) throws IOException {
        String json = EntityUtils.toString(response.getEntity());
        JSONObject jsonObj = new JSONObject(json);

        JSONObject house = jsonObj.getJSONObject("data");
        int dealType = house.getInt("dealType");
        if (dealType == 0)
            return new House(
                HouseOwner.KHANE_BE_DOOSH,
                house.getString("id"),
                (house.getString("buildingType").equals("آپارتمان") ? BuildingType.APARTMENT : BuildingType.VILLA),
                house.getInt("area"),
                house.getString("address"),
                house.getJSONObject("price").getFloat("sellPrice"),
                house.getString("phone"),
                house.getString("description"),
                house.getString("imageURL")
            );
        else
            return new House(
                HouseOwner.KHANE_BE_DOOSH,
                house.getString("id"),
                (house.getString("buildingType").equals("آپارتمان") ? BuildingType.APARTMENT : BuildingType.VILLA),
                house.getInt("area"),
                house.getString("address"),
                house.getJSONObject("price").getFloat("basePrice"),
                house.getJSONObject("price").getFloat("rentPrice"),
                house.getString("phone"),
                house.getString("description"),
                house.getString("imageURL")
            );
    }

    @Override
    protected String getContentType() {
        return "application/json";
    }
}
