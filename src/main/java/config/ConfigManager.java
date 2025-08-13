package config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
	private static Properties properties = new Properties();
	static {
		try {
			FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
			properties.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	public static String get(String key)
	{
		return properties.getProperty(key);
	}
}
