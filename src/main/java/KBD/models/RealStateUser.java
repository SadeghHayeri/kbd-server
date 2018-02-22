package KBD.models;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sadegh on 2/12/18.
 */
abstract public class RealStateUser extends User {
    private String apiAddress;

    public RealStateUser(String name, String apiAddress) {
        super(name);
        this.apiAddress = apiAddress;
    }

    public String getHouseListApiAddress() {
        return apiAddress + "/house";
    }

    public String getHouseApiAddress(String id) {
        return apiAddress + "/house/" + id;
    }

    public ArrayList<House> getHouses() {
        try {
            HttpResponse response = getHouseList();
            return parseGetHouseListResponse(response);
        } catch (Exception e) {
            Logger.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    public House getHouse(String id) {
        try {
            HttpResponse response = getSingleHouse(id);
            return parseGetHouseResponse(response);
        } catch (Exception e) {
            Logger.error(e.getMessage());
            return null;
        }
    }

    private HttpResponse getSingleHouse(String id) throws IOException {
        String url = getHouseApiAddress(id);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        get.setHeader("Content-type", getContentType());

        return httpClient.execute(get);
    }


    private HttpResponse getHouseList() throws IOException {
        String url = getHouseListApiAddress();

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        get.setHeader("Content-type", getContentType());

        return httpClient.execute(get);
    }

    protected abstract ArrayList<House> parseGetHouseListResponse(HttpResponse response) throws IOException;
    protected abstract House parseGetHouseResponse(HttpResponse response) throws IOException;
    protected abstract String getContentType();
}
