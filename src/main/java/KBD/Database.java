package KBD;

import KBD.models.House;
import KBD.models.IndividualUser;
import KBD.models.RealStateUser;
import KBD.models.realState.KhaneBeDoosh;

import java.util.ArrayList;

public class Database {
    private static ArrayList<House> houses = new ArrayList<>();
    private static ArrayList<IndividualUser> users = new ArrayList<>();
    private static ArrayList<RealStateUser> realStateUsers = new ArrayList<>();

    static {
        users.add(new IndividualUser("بهنام همایون", "behnam_homayoon", "09123456789", 0, "123"));
        realStateUsers.add(new KhaneBeDoosh("khaneBeDoosh", "http://acm.ut.ac.ir/khaneBeDoosh"));
    }

    public static void addHouse(House newHouse) {
        houses.add(newHouse);
    }
    public static IndividualUser getUser(int id) {
        return users.get(id);
    }

    public static ArrayList<House> getAllHouses() {
        ArrayList<House> allHouses = houses;
        // TODO: fetch real state's houses and add them to list
        return allHouses;
    }
}
