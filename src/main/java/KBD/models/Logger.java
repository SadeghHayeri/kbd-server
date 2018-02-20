package KBD.models;

public class Logger {
    private static String log = "";

    public static void info(String message) {
        log += message + "<br/>";
    }

    public static String getLog() {
        return log;
    }
}
