package KBD.services;

import KBD.models.House;
import KBD.models.Logger;
import KBD.models.RealStateUser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.ArrayList;

abstract public class HouseFetcher {
    private RealStateUser realStateUser;

    protected HouseFetcher(RealStateUser realStateUser) {
        this.realStateUser = realStateUser;
    }

    public ArrayList<House> getHouses() {
        try {
            HttpResponse response = getHouseList();
            return parseGetHouseListResponse(response);
        } catch (Exception e) {
            Logger.info(e.getMessage());
            return new ArrayList<>();
        }
    }

    public House getHouse(int id) {
        try {
            HttpResponse response = getSingleHouse(id);
            return parseGetHouseResponse(response);
        } catch (Exception e) {
            Logger.info(e.getMessage());
            return null;
        }
    }

    private HttpResponse getSingleHouse(int id) throws IOException {
        String url = realStateUser.getHouseApiAddress(id);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        get.setHeader("Content-type", getContentType());

        return httpClient.execute(get);
    }


    private HttpResponse getHouseList() throws IOException {
        String url = realStateUser.getHouseListApiAddress();

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        get.setHeader("Content-type", getContentType());

        return httpClient.execute(get);
    }

    protected abstract ArrayList<House> parseGetHouseListResponse(HttpResponse response);
    protected abstract House parseGetHouseResponse(HttpResponse response);
    protected abstract String getContentType();
}
