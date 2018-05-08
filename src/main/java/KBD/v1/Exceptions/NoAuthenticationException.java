package KBD.v1.Exceptions;

public class NoAuthenticationException extends RuntimeException {
    public NoAuthenticationException() {
        super();
    }
    public NoAuthenticationException(String filed) { super(filed); }
}

