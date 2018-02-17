package KBD;

import java.io.FileInputStream;
import java.util.Properties;

public class Config {
    static Properties configFile = new Properties();

    static {
        try {
            configFile.load(new FileInputStream("../../config.cfg"));
        }catch(Exception eta){
            eta.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return configFile.getProperty(key);
    }
}