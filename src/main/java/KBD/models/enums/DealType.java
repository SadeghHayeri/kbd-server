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

    public int toInteger() {
        if (this == BUY)
            return 0;
        else
            return 1;
    }

    public static DealType parseDealType(String type) {
        return parseDealType(Integer.parseInt(type));
    }
}
