package KBD;

import KBD.models.House;
import KBD.models.IndividualUser;
import KBD.models.RealStateUser;
import KBD.models.enums.BuildingType;
import KBD.models.enums.DealType;
import KBD.models.realState.KhaneBeDoosh;

import java.util.ArrayList;

public class Database {
    private static ArrayList<House> houses = new ArrayList<>();
    private static ArrayList<IndividualUser> users = new ArrayList<>();
    private static ArrayList<RealStateUser> realStateUsers = new ArrayList<>();

    static {
        users.add(new IndividualUser("بهنام همایون", "behnam_homayoon", "09123456789", 0, "123"));
        realStateUsers.add(new KhaneBeDoosh("khaneBeDoosh", "http://acm.ut.ac.ir/khaneBeDoosh"));

        houses.add(new House(BuildingType.VILLA, 100, "be to che", 1500.5f, "09333564933", "nadare"));
        houses.add(new House(BuildingType.APARTMENT, 102, "address", 100.5f, "09333564933", "nadare"));
        houses.add(new House(BuildingType.APARTMENT, 200, "fdsf", 200.5f, "09333564933", "nadare"));
        houses.add(new House(BuildingType.APARTMENT, 1000, "fsfsfsfsd", 2500.5f, "09333564933", "nadare"));
        houses.add(new House(BuildingType.VILLA, 50, "kkkk f", 1550.5f, "09333564933", "nadare"));
        houses.add(new House(BuildingType.VILLA, 200, "rrrr", 1700.5f, "09333564933", "nadare"));
        houses.add(new House(BuildingType.APARTMENT, 150, "salam", 5000.5f, "09333564933", "nadare"));
        houses.add(new House(BuildingType.VILLA, 120, "khone", 150.5f, "09333564933", "nadare"));

        houses.add(new House(BuildingType.VILLA, 100, "be to che", 1500.5f, 100.5f, "09333564933", "nadare"));
        houses.add(new House(BuildingType.APARTMENT, 102, "address", 100.5f, 500.5f, "09333564933", "nadare"));
        houses.add(new House(BuildingType.APARTMENT, 200, "fdsf", 200.5f, 120.5f, "09333564933", "nadare"));
        houses.add(new House(BuildingType.APARTMENT, 1000, "fsfsfsfsd", 2500.5f, 200.5f, "09333564933", "nadare"));
        houses.add(new House(BuildingType.VILLA, 50, "kkkk f", 1550.5f, 150.5f, "09333564933", "nadare"));
        houses.add(new House(BuildingType.VILLA, 200, "rrrr", 1700.5f, 150.5f, "09333564933", "nadare"));
        houses.add(new House(BuildingType.APARTMENT, 150, "salam", 5000.5f, 500.5f, "09333564933", "nadare"));
        houses.add(new House(BuildingType.VILLA, 120, "khone", 150.5f, 300.5f, "09333564933", "nadare"));
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

    public static ArrayList<House> getHouses() {
        ArrayList<House> houses = getOwnHouses();
        houses.addAll(getRealStateHouses());

        return houses;
    }

    public static ArrayList<House> getHouses(int minimumArea, BuildingType buildingType, DealType dealType, float maximumPrice) {
        ArrayList<House> houses = getOwnHouses(minimumArea, buildingType, dealType, maximumPrice);
        houses.addAll(getRealStateHouses(minimumArea, buildingType, dealType, maximumPrice));

        return houses;
    }

    public static ArrayList<House> getOwnHouses() {
        return houses;
    }

    public static ArrayList<House> getOwnHouses(int minimumArea, BuildingType buildingType, DealType dealType, float maximumPrice) {
        return sampleFilter(houses, minimumArea, buildingType, dealType, maximumPrice);
    }

    private static ArrayList<House> getRealStateHouses() {
        ArrayList<House> allHouses = new ArrayList<>();
        // TODO: fetch real state's houses and add them to list
        return allHouses;
    }

    private static ArrayList<House> getRealStateHouses(int minimumArea, BuildingType buildingType, DealType dealType, float maximumPrice) {
        ArrayList<House> allHouses = getRealStateHouses();
        return sampleFilter(allHouses, minimumArea, buildingType, dealType, maximumPrice);
    }

    private static ArrayList<House> sampleFilter(ArrayList<House> data, int minimumArea, BuildingType buildingType, DealType dealType, float maximumPrice) {
        ArrayList<House> result = new ArrayList<>();

        for (House house : data) {
            if (house.getBuildingType() == buildingType) {
                if (house.getArea() >= minimumArea) {
                    if (house.getDealType() == DealType.BUY) {
                        if (house.getSellPrice() <= maximumPrice)
                            result.add(house);
                    } else if (house.getDealType() == DealType.RENTAL) {
                        if (house.getRentPrice() <= maximumPrice)
                            result.add(house);
                    }
                }
            }
        }

        return result;
    }
}
