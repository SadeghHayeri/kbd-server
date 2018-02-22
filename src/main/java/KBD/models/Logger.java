package KBD.models;

public class Logger {
    private static String log = "";

    public static void info(String message) {
        log += message + "<br/>";
    }

    public static void error(String message) {
        message = "<span style='color:red'>ERROR OCCURRED: </span>" + message;
        info(message);
    }

    public static String getLog() {
        return log;
    }
}
