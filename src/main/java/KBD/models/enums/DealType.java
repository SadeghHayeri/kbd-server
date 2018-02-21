package KBD.models.enums;

public enum DealType {
    BUY,
    RENTAL;

    public static DealType parseDealType(int type) {
        if (type == 0)
            return BUY;
        else
            return RENTAL;
    }

    public static DealType parseDealType(String type) {
        return parseDealType(Integer.parseInt(type));
    }
}
