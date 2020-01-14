package com.wcpe.OreGifts;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import me.clip.placeholderapi.PlaceholderAPI;

public class Listeners implements Listener {
	private final BlockFace[] faces = new BlockFace[] { BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH,
			BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };

	public static List<GiftsData> Blockdata = new ArrayList<>();

	@EventHandler
	public void FromTo(BlockFromToEvent e) {
		Block To = e.getToBlock();
		Block block = e.getBlock();
		// 判断世界是否原石
		if (!spawnCobble(block, To))
			return;
		List<String> worlds = Main.LoadConfig().getConfig().getStringList("Worlds");
		// 判断世界是否在启用列表
		if (!(worlds.contains(e.getBlock().getLocation().getWorld().getName())))
			return;
		// 概率总计
		List<Double> TotalChance = new ArrayList<>(Main.getGifts().size());
		try {
		for (Gifts gift : Main.getGifts()) {
			double Chance = gift.getChance();
			if (Chance < 0)
				Chance = 0;
			TotalChance.add(Chance);
		}
		
		// 随机抽取一个索引id
		int IndexId = Random(TotalChance);
		try {
			String material = Main.getGifts().get(IndexId).getMaterial().toUpperCase();

			FileConfiguration config = Main.LoadConfig().getConfig();
			// 如果满足为gifts 将存储
			if (containsGift(IndexId, config)) {
				int x = To.getX();
				int y = To.getY();
				int z = To.getZ();
				String world = To.getWorld().getName();
				Location loc = new Location(Bukkit.getWorld(world), x, y, z);
				String name = Main.getGifts().get(IndexId).getName();

				List<String> lore = Main.getGifts().get(IndexId).getLore();

				List<String> commands = Main.getGifts().get(IndexId).getCommands();

				String giftmaterial = Main.getGifts().get(IndexId).getGiftMaterial();

				Blockdata.add(new GiftsData(loc, IndexId, name, lore, commands, material, giftmaterial));
				try {
					Material valueOf = Material.valueOf(giftmaterial);
					Bukkit.getScheduler().runTask(Main.LoadConfig(), new Runnable() {
						@Override
						public void run() {
							try {
								To.setType(valueOf);
								To.getState().update(true, true);
							} catch (java.lang.IllegalArgumentException e) {
								Bukkit.getConsoleSender()
										.sendMessage("§4请检查OreGifts配置文件Chances列表第" + IndexId + "个物品中Material是否为方块！");
							}
						}
					});
				} catch (IllegalArgumentException ee) {
					Bukkit.getConsoleSender()
							.sendMessage("§4请检查OreGifts配置文件Chances列表第" + IndexId + "个物品Material配置是否有误！");
				}
			} else {
				try {
					Material valueOf = Material.valueOf(material);
					Bukkit.getScheduler().runTask(Main.LoadConfig(), new Runnable() {
						@Override
						public void run() {
							try {
								To.setType(valueOf);
								To.getState().update(true, true);
							} catch (java.lang.IllegalArgumentException e) {
								Bukkit.getConsoleSender()
										.sendMessage("§4请检查OreGifts配置文件Chances列表第" + IndexId + "个物品中Material是否为方块！");
							}
						}
					});
				} catch (IllegalArgumentException ee) {
					Bukkit.getConsoleSender()
							.sendMessage("§4请检查OreGifts配置文件Chances列表第" + IndexId + "个物品Material配置是否有误！");
				}
			}
		} catch (java.lang.NullPointerException eee) {
			Bukkit.getConsoleSender().sendMessage("§4请检查OreGifts配置文件Chances列表第" + IndexId + "个物品Material配置是否为空！");
		}
		}catch(java.lang.ArrayIndexOutOfBoundsException eee) {
			Bukkit.getServer().getConsoleSender().sendMessage("§b[OreGifts]§4请检查配置文件中是否从0开始一直往后");
		}
	}

	@EventHandler
	public void Break(BlockBreakEvent e) {
		Location loc = e.getBlock().getLocation();
		for (GiftsData data : Blockdata) {
			Location location = data.getLocation();
			if (location.equals(loc)) {
				e.setDropItems(false);
				String material = data.getMaterial().toUpperCase();
				Material valueOf = Material.valueOf(material);
				String name = data.getName();
				List<String> lore = data.getLore();
				ItemStack item = new ItemStack(valueOf);
				ItemMeta im = item.getItemMeta();
				
				im.setDisplayName(name);
				im.setLore(lore);
				
				
				
				item.setItemMeta(im);
				location.getWorld().dropItem(location, item);
				
				List<String> commands = data.getCommands();
				
				for(String st:commands) {
					String pd[] = st.split("]");
					if (pd[0].equals("[CMD")) {
						if (pd[0].equals("[CMD")) {
							pd[1] = pd[1].replaceAll("%player%", e.getPlayer().getName());
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), pd[1]);
						}
					} else if (pd[0].equals("[CHAT")) {
						if (pd[0].equals("[CHAT")) {
							if (PlaceholderAPIfile().getBoolean("PlaceholderAPI")) {
								pd[1] = PlaceholderAPI.setPlaceholders(e.getPlayer(), pd[1]);
							}
							e.getPlayer().chat(pd[1]);
						}
					} else if (pd[0].equals("[TITLE")) {
						if (pd[0].equals("[TITLE")) {
								if (PlaceholderAPIfile().getBoolean("PlaceholderAPI")) {
									pd[1] = PlaceholderAPI.setPlaceholders(e.getPlayer(), pd[1]);
									pd[2] = PlaceholderAPI.setPlaceholders(e.getPlayer(), pd[2]);
								}
								e.getPlayer().sendTitle(pd[1], pd[2], Integer.valueOf(pd[3]), Integer.valueOf(pd[4]),
										Integer.valueOf(pd[5]));
							
						}
					} else if (pd[0].equals("[ACTION")) {
						if (pd[0].equals("[ACTION")) {
								if (PlaceholderAPIfile().getBoolean("PlaceholderAPI")) {
									pd[1] = PlaceholderAPI.setPlaceholders(e.getPlayer(), pd[1]);
								}
								Actionbar.sendAction(e.getPlayer(), pd[1]);
							
						}
					} else if (pd[0].equals("[OP")) {
						if (pd[0].equals("[OP")) {
								boolean isop = e.getPlayer().isOp();
								try {
									Bukkit.getScheduler().runTask(Main.LoadConfig(), new Runnable() {
										public void run() {
											e.getPlayer().setOp(true);
											if (PlaceholderAPIfile().getBoolean("PlaceholderAPI")) {
												pd[1] = PlaceholderAPI.setPlaceholders(e.getPlayer().getPlayer(), pd[1]);
											}
											e.getPlayer().chat(pd[1]);
											e.getPlayer().setOp(isop);
										}
									});
									
								} catch (Exception eee) {
								} finally {
									e.getPlayer().setOp(isop);
								}
							
						}
					} else if (pd[0].equals("[Bd")) {
						Bukkit.broadcastMessage(pd[1]);
					} else {
						for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
							if (PlaceholderAPIfile().getBoolean("PlaceholderAPI")) {
								pd[0] = PlaceholderAPI.setPlaceholders(pl.getPlayer(), pd[0]);
							}
							pl.sendMessage(pd[0]);
						}
					}
				}
				
				Blockdata.remove(data);
				return;
			}
		}
	}

	public FileConfiguration PlaceholderAPIfile() {
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Main.LoadConfig().getDataFolder(), "PlaceholderAPI.yml"));
		return config;
	}
	
	public boolean spawnCobble(Block block, Block To) {
		Material type = block.getType();
		Material ID1 = (type == Material.WATER ? Material.LAVA : Material.WATER);
		Material ID2 = (type == Material.WATER ? Material.LAVA : Material.WATER);
		for (BlockFace face : faces) {
			Block r = To.getRelative(face, 1);
			if (r.getType() == ID1 || r.getType() == ID2) {
				return true;
			}
		}
		return false;
	}

	// 判断是直接放置的方块还是需要检测的gifts
	public boolean containsGift(int IndexId, FileConfiguration config) {
		String key = "Chances." + String.valueOf(IndexId) + ".";
		for (String st : config.getKeys(true)) {
			if (st.contains(key)) {
				if ((st.contains("Name")) || (st.contains("Lore")) || (st.contains("Commands"))
						|| (st.contains("GiftMaterial"))) {
					return true;
				}
			}
		}
		return false;
	}

	// 随机抽取
	public static int Random(List<Double> TotalChance) {

		if (TotalChance == null || TotalChance.isEmpty()) {
			return -1;
		}

		int size = TotalChance.size();

		// 计算总概率，这样可以保证不一定总概率是1
		double sumRate = 0d;
		for (double rate : TotalChance) {
			sumRate += rate;
		}

		// 计算每个物品在总概率的基础下的概率情况
		List<Double> sortOrignalRates = new ArrayList<>(size);
		Double tempSumRate = 0d;
		for (double rate : TotalChance) {
			tempSumRate += rate;
			sortOrignalRates.add(tempSumRate / sumRate);
		}

		// 得到索引
		double nextDouble = Math.random();
		sortOrignalRates.add(nextDouble);
		Collections.sort(sortOrignalRates);

		return sortOrignalRates.indexOf(nextDouble);
	}
}