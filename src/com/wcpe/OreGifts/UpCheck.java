package com.wcpe.OreGifts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;




public class UpCheck {
	public static Thread upCheck = new Thread(new Runnable() {
		public void run() {
			try {
				String version = UpCheck.upCheckVersion().getString("NewVersion");
				if (UpCheck.isLatestVersion()) {
					Bukkit.getServer().getConsoleSender()
							.sendMessage("§a[§bOregifts§a]§4当前不是最新版本 " + "§a最新版本:§8§l" + version);
					List<String> stringList = UpCheck.upCheckVersion().getStringList("Version." + version + ".Updata");
					for (String st : stringList) {
						Bukkit.getServer().getConsoleSender().sendMessage(st);
					}
				} else {
					Bukkit.getServer().getConsoleSender()
							.sendMessage("§a[§bOregifts§a]§a§lb§r§e(§0●§e'§8◡§e'§0●§e)§a§ld§r§e当前为最新版本 " + version
									+ " §a§lb§r§e(￣▽￣)§a§ld§r§8§l");
				}
			}catch(IllegalThreadStateException e) {
				System.out.println("§4检查更新失败！！！");
			}
		}
	});
	
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
