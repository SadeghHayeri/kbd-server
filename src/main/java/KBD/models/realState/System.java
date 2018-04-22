package KBD.models.realState;

import KBD.models.House;
import KBD.models.RealStateUser;
import KBD.models.enums.HouseOwner;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.ArrayList;

public class System extends RealStateUser {
    public System() {
        super(HouseOwner.SYSTEM.toString(), "");
    }

    public System(int id) {
        super(id, HouseOwner.SYSTEM.toString(), "");
    }

    @Override
    protected ArrayList<House> parseGetHouseListResponse(String data) throws IOException {
        return null;
    }

    @Override
    protected House parseGetHouseResponse(String data) throws IOException {
        return null;
    }

    @Override
    protected String getContentType() {
        return null;
    }

    @Override
    protected long getExpireTimestamp(String data) throws IOException {
        return 0;
    }
}
