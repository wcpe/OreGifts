package com.wcpe.OreGifts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.bukkit.configuration.file.YamlConfiguration;

public class UpCheck {
	public static YamlConfiguration upCheckVersion() {
		YamlConfiguration load = null;
		try {
			URL url = new URL("https://wcpe.github.io/Oregifts.yml");
			HttpURLConnection conn = (HttpURLConnection)url.openConnection(); 
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			load = YamlConfiguration.loadConfiguration(br);
			is.close();
			conn.disconnect();
		} catch (IOException e) {
			System.out.println("§4检查更新失败！！！");
		}
		return load;
	}
	public static boolean isLatestVersion() {
		boolean isLatest = false;
		double latest = Double.valueOf(upCheckVersion().getString("NewVersion"));
		double current = Main.Version;
		if(current < latest) {
			isLatest = true;
		}
		return isLatest;
	}
}
