package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class GameConfig {
	Properties properties = new Properties();
	
	public void saveConfig(String key, int value) {
			try {
				String path = "config.xml";
				File file = new File(path);
				boolean exist = file.exists();
				if(!exist) file.createNewFile();
				OutputStream write = new FileOutputStream(path);
				properties.setProperty(key, Integer.toString(value));
				properties.storeToXML(write, "Options");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public void loadConfig(String path) {
		try {
			InputStream read = new FileInputStream(path);
			properties.loadFromXML(read);
			String best = properties.getProperty("best");
			setBest(Integer.parseInt(best));
			read.close();
		} catch (IOException e) {
			saveConfig("best", 0);
			loadConfig(path);
		}
	}
	
	private void setBest(int best) {
		Game.bestScore = best;
	}
}
