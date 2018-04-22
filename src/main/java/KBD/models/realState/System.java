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
    protected long getExpireTimestamp(HttpResponse response) throws IOException {
        return 0;
    }

    @Override
    protected ArrayList<House> parseGetHouseListResponse(HttpResponse response) throws IOException {
        return null;
    }

    @Override
    protected House parseGetHouseResponse(HttpResponse response) throws IOException {
        return null;
    }

    @Override
    protected String getContentType() {
        return null;
    }
}
