package KBD.models.enums;

public enum HouseOwner {
    SYSTEM,
    KHANE_BE_DOOSH;

    @Override
    public String toString() {
        if(this == SYSTEM)
            return "system";
        else if (this == KHANE_BE_DOOSH)
            return "khane-be-doosh";
        else
            return null;
    }

    public static HouseOwner parseString(String owner) {
        if (owner.equals(SYSTEM.toString()))
            return SYSTEM;
        else if (owner.equals(KHANE_BE_DOOSH.toString()))
            return KHANE_BE_DOOSH;
        else
            return null; //TODO: use exceptions!
    }
}
