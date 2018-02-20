package KBD.models.enums;

public enum BuildingType {
    VILLA,
    APARTMENT;

    public static BuildingType parseBuildingType(int type) {
        if (type == 0)
            return VILLA;
        else
            return APARTMENT;
    }

    public static BuildingType parseBuildingType(String type) {
        return parseBuildingType(Integer.parseInt(type));
    }
}
