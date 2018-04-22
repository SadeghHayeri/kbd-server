package KBD.models;


import KBD.Database;
import KBD.models.enums.HouseOwner;
import KBD.models.realState.KhaneBeDoosh;
import KBD.models.realState.System;
import KBD.v1.Exceptions.NotFoundException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sadegh on 2/12/18.
 */
abstract public class RealStateUser extends User {
    private String apiAddress;

    private class Fetcher extends TimerTask {
        private RealStateUser realStateUser;
        public Fetcher(RealStateUser realStateUser) {
            super();
            this.realStateUser = realStateUser;
        }

        public void run() {
            realStateUser.fetchHouses();
        }
    }

    public RealStateUser(int id, String name, String apiAddress) {
        super(id, name);
        this.apiAddress = apiAddress;

        this.isSaved = true;
    }

    protected RealStateUser(String name, String apiAddress) {
        super(name);
        this.apiAddress = apiAddress;
    }

    public void save() {
        if(!isSaved)
            executeUpdate(
                    String.format(
                            "INSERT INTO %s (name, api_address) VALUES ('%s', '%s')",
                            Database.REALSTATE_USERS_TB, name, apiAddress
                    )
            );
        else if(isModified)
            executeUpdate(
                    String.format(
                            "UPDATE %s SET name = '%s', api_address = '%s' WHERE id = %d",
                            Database.REALSTATE_USERS_TB, name, apiAddress, id
                    )
            );
    }

    public void deleteHouses() {
        executeUpdate(
                String.format("DELETE FROM %s WHERE owner = %d", Database.HOUSES_TB, id)
        );
    }

    private static RealStateUser findByQuery(String query) {
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query);

            RealStateUser realStateUser = null;
            if (resultSet != null && resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String apiAddress = resultSet.getString("api_address");
                realStateUser = RealStateUser.make(id, name, apiAddress);
            }
            connection.close();
            return realStateUser;
        } catch (SQLException e) {
            Logger.error(e.getMessage());
            return null;
        }
    }

    public static RealStateUser find(String name) {
        return findByQuery(
                String.format(
                        "SELECT * FROM %s WHERE name = '%s'", Database.REALSTATE_USERS_TB, name));
    }

    public static RealStateUser find(int id) {
        return findByQuery(String.format("SELECT * FROM %s WHERE id = %d", Database.REALSTATE_USERS_TB, id));
    }

    public static ArrayList<RealStateUser> list() {
        ArrayList<RealStateUser> realStateUsers = new ArrayList<>();
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(
                    String.format(
                            "SELECT * FROM %s WHERE name != '%s'",
                            Database.REALSTATE_USERS_TB, HouseOwner.SYSTEM.toString()
                    )
            );

            if (resultSet != null && resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String apiAddress = resultSet.getString("api_address");
                realStateUsers.add(RealStateUser.make(id, name, apiAddress));
            }
            connection.close();
        } catch (SQLException e) {
            Logger.error(e.getMessage());
        }
        return realStateUsers;
    }

    private static RealStateUser make(int id, String name, String apiAddress) {
        RealStateUser realStateUser = null;
        if (name.equals(HouseOwner.SYSTEM.toString()))
            realStateUser = new System(id);
        else if (name.equals(HouseOwner.KHANE_BE_DOOSH.toString()))
            realStateUser = new KhaneBeDoosh(id, apiAddress);
        return realStateUser;
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
            //  startScheduler(response);
            return parseGetHouseListResponse(response);
        } catch (Exception e) {
            Logger.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    private void startScheduler(HttpResponse response) throws IOException {
        Timer timer = new Timer();
        long expireTimestamp = getExpireTimestamp(response);
        timer.schedule(new Fetcher(this), expireTimestamp - java.lang.System.currentTimeMillis());
    }

    protected abstract long getExpireTimestamp(HttpResponse response) throws IOException;

    public House getHouse(String id) {
        try {
            HttpResponse response = getSingleHouse(id);
            return parseGetHouseResponse(response);
        } catch (Exception e) {
            Logger.error(e.getMessage());
            throw new NotFoundException("realstate bad response, House not found!");
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

    public void fetchHouses() {
        deleteHouses();
        for (House house: getHouses())
            house.save();
    }
}
