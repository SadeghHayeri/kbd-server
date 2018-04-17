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

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by sadegh on 2/12/18.
 */
abstract public class RealStateUser extends User {
    private String apiAddress;

    private static final String TABLE_NAME = "realstate_users";

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
        executeUpdate(
                String.format("INSERT INTO %s (name, api_address) VALUES ('%s', '%s')", TABLE_NAME, name, apiAddress)
        );
    }

    public void deleteHouses() {
        executeUpdate(
                String.format("DELETE FROM %s WHERE owner == %d", House.TABLE_NAME, id)
        );
    }

    public static RealStateUser find(String name) {
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(
                    String.format("SELECT * FROM %s WHERE name = '%s'", TABLE_NAME, name)
            );

            RealStateUser realStateUser = null;
            if (resultSet != null && resultSet.next()) {
                int id = resultSet.getInt("id");
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

    public static ArrayList<RealStateUser> list() {
        ArrayList<RealStateUser> realStateUsers = new ArrayList<>();
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(
                    String.format("SELECT * FROM %s WHERE name != '%s'", TABLE_NAME, HouseOwner.SYSTEM.toString())
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

    public static RealStateUser make(int id, String name, String apiAddress) {
        RealStateUser realStateUser = null;
        if (name.equals(HouseOwner.SYSTEM.toString()))
            realStateUser = new System(id, name, apiAddress);
        else if (name.equals(HouseOwner.KHANE_BE_DOOSH.toString()))
            realStateUser = new KhaneBeDoosh(id, name, apiAddress);
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
}
