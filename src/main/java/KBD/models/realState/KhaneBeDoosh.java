package KBD.models.realState;

import KBD.models.House;
import KBD.models.RealStateUser;
import KBD.models.enums.BuildingType;
import KBD.models.enums.HouseOwner;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class KhaneBeDoosh extends RealStateUser {
    public KhaneBeDoosh(int id, String apiAddress) {
        super(id, HouseOwner.KHANE_BE_DOOSH.toString(), apiAddress);
    }

    public KhaneBeDoosh(String apiAddress) {
        super(HouseOwner.KHANE_BE_DOOSH.toString(), apiAddress);
    }

    @Override
    protected long getExpireTimestamp(String data) throws IOException {
        JSONObject jsonObj = new JSONObject(data);
        return jsonObj.getLong("expireTime");
    }

    @Override
    protected ArrayList<House> parseGetHouseListResponse(String rData) throws IOException {
        JSONObject jsonObj = new JSONObject(rData);

        ArrayList<House> houses = new ArrayList<>();

        JSONArray data = jsonObj.getJSONArray("data");
        for(Object object: data){
            JSONObject house = (JSONObject) object;
            int dealType = house.getInt("dealType");
            if (dealType == 0)
                houses.add(
                    new House(
                        id,
                        house.getString("id"),
                        (house.getString("buildingType").equals("آپارتمان") ? BuildingType.APARTMENT : BuildingType.VILLA),
                        house.getInt("area"),
                        house.getString("address"),
                        house.getJSONObject("price").getInt("sellPrice"),
                        "",
                        "",
                        house.getString("imageURL"),
                        false
                    )
                );
            else
                houses.add(
                    new House(
                        id,
                        house.getString("id"),
                        (house.getString("buildingType").equals("آپارتمان") ? BuildingType.APARTMENT : BuildingType.VILLA),
                        house.getInt("area"),
                        house.getString("address"),
                        house.getJSONObject("price").getInt("basePrice"),
                        house.getJSONObject("price").getInt("rentPrice"),
                        "",
                        "",
                        house.getString("imageURL"),
                        false
                    )
                );
        }

        return houses;
    }

    @Override
    protected House parseGetHouseResponse(String data) throws IOException {
        JSONObject jsonObj = new JSONObject(data);

        JSONObject house = jsonObj.getJSONObject("data");
        int dealType = house.getInt("dealType");
        if (dealType == 0)
            return new House(
                id,
                house.getString("id"),
                (house.getString("buildingType").equals("آپارتمان") ? BuildingType.APARTMENT : BuildingType.VILLA),
                house.getInt("area"),
                house.getString("address"),
                house.getJSONObject("price").getInt("sellPrice"),
                house.getString("phone"),
                house.getString("description"),
                house.getString("imageURL"),
                false
            );
        else
            return new House(
                id,
                house.getString("id"),
                (house.getString("buildingType").equals("آپارتمان") ? BuildingType.APARTMENT : BuildingType.VILLA),
                house.getInt("area"),
                house.getString("address"),
                house.getJSONObject("price").getInt("basePrice"),
                house.getJSONObject("price").getInt("rentPrice"),
                house.getString("phone"),
                house.getString("description"),
                house.getString("imageURL"),
                false
            );
    }

    @Override
    protected String getContentType() {
        return "application/json";
    }
}
