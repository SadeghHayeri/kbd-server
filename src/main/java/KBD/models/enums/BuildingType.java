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

    public int toInteger() {
        if (this == VILLA)
            return 0;
        else
            return 1;
    }

    public static BuildingType parseBuildingType(String type) {
        return parseBuildingType(Integer.parseInt(type));
    }
}
