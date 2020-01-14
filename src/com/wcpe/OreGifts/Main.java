package com.wcpe.OreGifts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private static List<Gifts> gifts = new ArrayList<>();

	private static JavaPlugin LoadConfig;

	public static List<Gifts> getGifts() {
		return gifts;
	}

	public static JavaPlugin LoadConfig() {
		if (LoadConfig != null) {
			return LoadConfig;
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public void onEnable() {
		LoadConfig = this;
		papi();
		
		getServer().getConsoleSender().sendMessage("§b[OreGifts]§c载入矿物ing！");

		Bukkit.getPluginManager().registerEvents(new com.wcpe.OreGifts.Listeners(), this);
		Bukkit.getPluginCommand("oregifts").setExecutor(new com.wcpe.OreGifts.Commands());
		saveDefaultConfig();
		getServer().getConsoleSender().sendMessage("§b[OreGifts]§c载入完成！");
		loadGiftsBlockData();
		loadGifts();
	}

	@Override
	public void onDisable() {

		savaGiftsBlockData();

		getServer().getConsoleSender().sendMessage("§b[OreGifts]§c插件卸载完成！");
	}

	// 读取物品几率等信息 并且存入gifts
	public void loadGifts() {
		try {
			for (int t = 0; t >= 0; t++) {
				if (!this.getConfig().isSet("Chances." + String.valueOf(t)))
					break;
				String Name = this.getConfig().getString("Chances." + t + ".Name");
				String Material = this.getConfig().getString("Chances." + t + ".Material");
				List<String> Lore = this.getConfig().getStringList("Chances." + t + ".Lore");
				List<String> Commands = this.getConfig().getStringList("Chances." + t + ".Commands");
				double chance = this.getConfig().getDouble("Chances." + t + ".Chance");
				String giftmaterial = this.getConfig().getString("Chances." + t + ".GiftMaterial");
				gifts.add(new Gifts(t, Name, Material, Lore, Commands, chance, giftmaterial));
			}
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
			getServer().getConsoleSender().sendMessage("§b[OreGifts]§4请检查配置文件中是否从0开始一直往后");
		}

	}

	void papi() {
		File PlaceholderAPIFile = new File(getDataFolder(), "PlaceholderAPI.yml");
		FileConfiguration Papi = YamlConfiguration.loadConfiguration(PlaceholderAPIFile);
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) { // 如果是null，意味着插件没有安装，因为服务器获取不到PAPI
			getServer().getConsoleSender().sendMessage("§b[OreGifts]§aPlaceholderAPI已安装！");
			Papi.set("PlaceholderAPI", true);
			try {
				Papi.save(PlaceholderAPIFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			getServer().getConsoleSender().sendMessage("§b[OreGifts]§4您未安装PlaceholderAPI！");
			Papi.set("PlaceholderAPI", false);
			try {
				Papi.save(PlaceholderAPIFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 读取Gifts方块 信息 并且存入Blockdata
	public void loadGiftsBlockData() {
		File file = new File(this.getDataFolder(), "data.yml");
		FileConfiguration data = YamlConfiguration.loadConfiguration(file);
		for (String st : data.getKeys(true)) {
			if ((st.contains("Blockdata.")) && (!st.contains("Name")) && (!st.contains("Material"))
					&& (!st.contains("GiftMaterial")) && (!st.contains("Lore")) && (!st.contains("Commands"))
					&& (!st.contains("IndexID"))) {
				String substring = st.substring(10, st.length());
				String pd[] = substring.split(";");
				String world = pd[0];
				int x = Integer.valueOf(pd[1]);
				int y = Integer.valueOf(pd[2]);
				int z = Integer.valueOf(pd[3]);
				Location loc = new Location(Bukkit.getWorld(world), x, y, z);
				int IndexID = data.getInt("Blockdata." + substring + ".IndexID");
				String name = data.getString("Blockdata." + substring + ".Name");
				String material = data.getString("Blockdata." + substring + ".Material");
				String giftmaterial = data.getString("Blockdata." + substring + ".GiftMaterial");
				List<String> lore = data.getStringList("Blockdata." + substring + ".Lore");
				List<String> commands = data.getStringList("Blockdata." + substring + ".Commands");

				Listeners.Blockdata.add(new GiftsData(loc, IndexID, name, lore, commands, material, giftmaterial));
			}
		}

	}

	// 存入data文件
	public void savaGiftsBlockData() {
		try {
			File file = new File(Main.LoadConfig().getDataFolder(), "data.yml");
			FileConfiguration data = YamlConfiguration.loadConfiguration(file);
			data.set("Blockdata", null);
			for (GiftsData bd : Listeners.Blockdata) {
				Location loc = bd.getLocation();
				int x = (int) loc.getX();
				int y = (int) loc.getY();
				int z = (int) loc.getZ();
				String world = loc.getWorld().getName();
				int IndexID = bd.getIndex();
				String name = bd.getName();
				String material = bd.getMaterial();
				String giftmaterial = bd.getGiftMaterial();
				List<String> lore = bd.getLore();
				List<String> commands = bd.getCommands();

				String path = "Blockdata." + world + ";" + x + ";" + y + ";" + z;
				data.set(path + ".IndexID", IndexID);
				data.set(path + ".Name", name);
				data.set(path + ".Material", material);
				data.set(path + ".GiftMaterial", giftmaterial);
				data.set(path + ".Lore", lore);
				data.set(path + ".Commands", commands);

			}
			try {
				data.save(file);
			} catch (IOException e1) {
				getServer().getConsoleSender().sendMessage("§b[OreGifts]§4Gifts数据存储出现问题！");
			}
		} catch (java.lang.NullPointerException e) {
			getServer().getConsoleSender().sendMessage("§b[OreGifts]§4Gifts数据存储出现问题,空数据！");
		}

		getServer().getConsoleSender().sendMessage("§b[OreGifts]§cGifts数据存储完成！");
	}
}