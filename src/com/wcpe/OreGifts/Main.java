package com.wcpe.OreGifts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.wcpe.OreGifts.Entity.Gifts;
import com.wcpe.OreGifts.Entity.Ore;

public final class Main extends JavaPlugin implements Listener {
	public final Runnable updata = () -> {
		try {
			String version = UpCheck.upCheckVersion().getString("NewVersion");
			if (UpCheck.isLatestVersion()) {
				log("§4当前不是最新版本 " + "§a最新版本:§8§l" + version);
				List<String> stringList = UpCheck.upCheckVersion().getStringList("Version." + version + ".Updata");
				for (String st : stringList) {
					log(st.replaceAll("%version%", "" + Version));
				}
			} else {
				log("§a§lb§r§e(§0●§e'§8◡§e'§0●§e)§a§ld§r§e当前为最新版本 " + version + " §a§lb§r§e(￣▽￣)§a§ld§r§8§l");
			}
		} catch (IllegalThreadStateException e) {
			log("§4检查更新失败！！！");
		}
	};
	private boolean hasPapi;
	private boolean checkVersionEnable;
	private int checkVersionTime;
	private boolean autoSaveDataEnable;
	private int autoSaveDataTime;
	private List<String> worlds;

	private List<Ore> ores = new ArrayList<>();

	public List<Ore> getOres() {
		return ores;
	}

	private HashMap<String, Gifts> gifts = new HashMap<>();

	public void log(String log) {
		getServer().getConsoleSender().sendMessage("§a[§e" + this.getName() + "§a]§r" + log);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			if (args[0].equals("reload")) {
				if (sender instanceof Player) {
					if (sender.hasPermission("oregifts.reload")) {
						sender.sendMessage("§b[OreGifts]§c开始重载配置文件!");
						saveData();
						relConfig();
					} else {
						sender.sendMessage("§b[OreGifts]§4c您没有权限使用这个指令！");
					}
				} else {
					sender.sendMessage("§b[OreGifts]§c开始重载配置文件!");
					saveData();
					relConfig();
				}
			}
		} else {
			sender.sendMessage("§b[OreGifts]§e/oregifts reload 重载配置文件!");
		}
		return false;
	}

	final public static double Version = 3.1;
	private static Main instance;

	public static Main getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		timeUpCheck();
		new bStats(this, 7498);
		saveDefaultConfig();
		ConfigurationSerialization.registerClass(Gifts.class);
		relConfig();
		loadPapi();
		log("§c注冊监听器ing！");
		version();
		log("§c载入矿物ing！");
		getServer().getPluginCommand("oregifts").setExecutor(this);
		log(" Enable！！！");
	}

	private void relConfig() {
		reloadConfig();
		checkVersionEnable = this.getConfig().getBoolean("CheckVersion.Enable");
		checkVersionTime = this.getConfig().getInt("CheckVersion.Time");
		worlds = this.getConfig().getStringList("Worlds");
		int a = 0;
		for (String s : this.getConfig().getConfigurationSection("Chances").getKeys(false)) {
			ores.add(new Ore(this.getConfig().getString("Chances." + s + ".Name"),
					this.getConfig().getString("Chances." + s + ".Material"),
					this.getConfig().getString("Chances." + s + ".GiftMaterial"),
					this.getConfig().getStringList("Chances." + s + ".Lore"),
					this.getConfig().getStringList("Chances." + s + ".Commands"),
					this.getConfig().getDouble("Chances." + s + ".Chance"),
					this.getConfig().getInt("Chances." + s + ".Amount")));
			a++;
		}
		log("§a成功加载§e" + a + "§a个配置");
		reloadData();
		timeSaveData();
	}

	public boolean isHasPapi() {
		return hasPapi;
	}

	private void reloadData() {
		File f = new File(this.getDataFolder(), "data.yml");
		YamlConfiguration data = YamlConfiguration.loadConfiguration(f);
		Object obj = data.get("data");
		if (obj != null) {
			for (String s : data.getConfigurationSection("data").getKeys(false)) {
				gifts.put(s, (Gifts) data.get("data." + s));
			}
		}

	}

	private void saveData() {
		File f = new File(this.getDataFolder(), "data.yml");
		YamlConfiguration data = YamlConfiguration.loadConfiguration(f);
		data.set("data", gifts);
		try {
			data.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void version() {
		String a = getServer().getVersion();
		String pd[] = a.substring(a.length() - 7, a.length() - 1).split("\\.");
		if (Integer.valueOf(pd[0] + pd[1] + pd[2]) <= 1122) {
			Bukkit.getPluginManager().registerEvents(new List1_12(this), this);
			log("§c自动判断版本<=1.12.2自动更改监听！");
		} else {
			Bukkit.getPluginManager().registerEvents(new List1_13(this), this);
			log("§c自动判断版本>1.12.2自动更改监听！");
		}
	}

	private void loadPapi() {
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			log("§aPlaceholderAPI已安装！");
			hasPapi = true;
		} else {
			log("§4您未安装PlaceholderAPI！");
		}
	}

	private void timeSaveData() {
		if (autoSaveDataEnable) {
			Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
				new Thread(() -> {
					saveData();
				});
			}, autoSaveDataTime * 20, autoSaveDataTime * 20);
		}
	}

	private void timeUpCheck() {
		if (checkVersionEnable) {
			Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
				new Thread(this.updata);
			}, 0, checkVersionTime * 20);
		}
	}

	@Override
	public void onDisable() {
		saveData();
		log(" Disable！！！");
	}

	public List<String> getWorlds() {
		return worlds;
	}

	public Ore Random(List<Ore> ore) {
		List<Double> TotalChance = new ArrayList<>(ore.size());
		for (Ore o : ore) {
			double Chance = o.getChance();
			if (Chance < 0)
				Chance = 0;
			TotalChance.add(Chance);
		}
		if (TotalChance == null || TotalChance.isEmpty()) {
			return null;
		}
		int size = TotalChance.size();
		double sumRate = 0d;
		for (double rate : TotalChance) {
			sumRate += rate;
		}
		List<Double> sortOrignalRates = new ArrayList<>(size);
		Double tempSumRate = 0d;
		for (double rate : TotalChance) {
			tempSumRate += rate;
			sortOrignalRates.add(tempSumRate / sumRate);
		}
		double nextDouble = Math.random();
		sortOrignalRates.add(nextDouble);
		Collections.sort(sortOrignalRates);
		return ore.get(sortOrignalRates.indexOf(nextDouble));
	}

	public HashMap<String, Gifts> getGifts() {
		return gifts;
	}
}