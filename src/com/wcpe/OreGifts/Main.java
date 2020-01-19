package com.wcpe.OreGifts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.wcpe.OreGifts.Obj.Gifts;
import com.wcpe.OreGifts.Obj.GiftsData;
import com.wcpe.OreGifts.Utils.UpCheck;


public class Main extends JavaPlugin {

	final public static double Version = 0.9;
	
	public static List<Gifts> gifts = new ArrayList<>();

	public static HashMap<String,GiftsData> Blockdata = new HashMap<String,GiftsData>();

	private static JavaPlugin LoadConfig;
	public static JavaPlugin LoadConfig() {
		if (LoadConfig != null) {
			return LoadConfig;
		} else {
			throw new IllegalStateException();
		}
	}
///////////////////////////////////
	@Override
	public void onEnable() {
		LoadConfig = this;
		papi();
		timeUpCheck();
		getServer().getConsoleSender().sendMessage("§b[OreGifts]§c载入矿物ing！");

		version();
		
		Bukkit.getPluginCommand("oregifts").setExecutor(new com.wcpe.OreGifts.Commands());
		saveDefaultConfig();
		
		loadGiftsBlockData();
		
		
		loadGifts();
		
		getServer().getConsoleSender().sendMessage("§b[OreGifts]§c载入完成！");
		
	}
///////////////////////////////////	
	void version() {
		String a = getServer().getVersion();
		String subSequence = a.substring(a.length()-7, a.length()-1);
		String pd[] = subSequence.split("\\.");
		String version = pd[0]+pd[1]+pd[2];
		if(Integer.valueOf(version)<=1122) {
			Bukkit.getPluginManager().registerEvents(new com.wcpe.OreGifts.List1_12(), this);
			getServer().getConsoleSender().sendMessage("§b[OreGifts]§c自动判断版本<=1.12.2自动更改监听！");
		}else {
			Bukkit.getPluginManager().registerEvents(new com.wcpe.OreGifts.List1_13(), this);
			getServer().getConsoleSender().sendMessage("§b[OreGifts]§c自动判断版本>1.12.2自动更改监听！");
		}
	}
///////////////////////////////////
	@Override
	public void onDisable() {

		savaGiftsBlockData();

		getServer().getConsoleSender().sendMessage("§b[OreGifts]§c插件卸载完成！");
	}
////////////////////////////////////
	// 读取物品几率等信息 并且存入gifts
	void loadGifts() {
		try {
			for (int t = 0; t >= 0; t++) {
				if (this.getConfig().getString("Chances." + String.valueOf(t)) == null)
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
	
	// 读取Gifts方块 信息 并且存入Blockdata
		void loadGiftsBlockData() {
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
					String key = world+";"+x+";"+y+";"+z;
					Blockdata.put(key,new GiftsData(loc, IndexID, name, lore, commands, material, giftmaterial));
				}
			}

		}

		// 存入data文件
		void savaGiftsBlockData() {
			try {
				File file = new File(Main.LoadConfig().getDataFolder(), "data.yml");
				FileConfiguration data = YamlConfiguration.loadConfiguration(file);
				data.set("Blockdata", null);
				for (Entry<String, GiftsData> entr : Blockdata.entrySet()) {
					GiftsData bd = entr.getValue();
					Location loc = bd.getLocation();
					int x = (int) loc.getX();
					int y = (int) loc.getY();
					int z = (int) loc.getZ();
					String world = loc.getWorld().getName();
					int IndexID = bd.getIndex();
					String name = bd.getName();
					String material = bd.getMaterial().toString();
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
				getServer().getConsoleSender().sendMessage("§b[OreGifts]§4Gifts空数据！");
			}

			getServer().getConsoleSender().sendMessage("§b[OreGifts]§cGifts数据存储完成！");
		}
	
	void timeUpCheck() {

		if (this.getConfig().getBoolean("CheckVersion.Enable")) {
			Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
				@Override
				public void run() {
					getServer().getConsoleSender().sendMessage("§a[§bOregifts§a]§a正在检查是否有新版本...");
					Thread upCheck = new Thread(new Runnable() {
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
					upCheck.start();
				}
			}, 0, this.getConfig().getLong("CheckVersion.Time"));
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

	
}