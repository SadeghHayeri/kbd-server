package KBD.v1.Exceptions;

public class FieldNotFoundException extends RuntimeException {
    public FieldNotFoundException() {
        super();
    }
    public FieldNotFoundException(String filed) { super(filed); }
}

