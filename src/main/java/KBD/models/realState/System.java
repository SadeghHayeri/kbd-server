package KBD.models.realState;

import KBD.models.House;
import KBD.models.RealStateUser;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.ArrayList;

public class System extends RealStateUser {
    public System(int id, String name, String apiAddress) {
        super(id, name, apiAddress);
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
