package KBD.models.enums;

import KBD.v1.Exceptions.NotFoundException;

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
            throw new NotFoundException("House owner not found!");
    }

    public static HouseOwner parseString(String owner) {
        if (owner.equals(SYSTEM.toString()))
            return SYSTEM;
        else if (owner.equals(KHANE_BE_DOOSH.toString()))
            return KHANE_BE_DOOSH;
        else
            throw new NotFoundException("House owner " + owner + " not found!");
    }
}
