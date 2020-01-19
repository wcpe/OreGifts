package com.wcpe.OreGifts;

import java.io.File;
import java.util.ArrayList;
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

import com.wcpe.OreGifts.Obj.Gifts;
import com.wcpe.OreGifts.Obj.GiftsData;
import com.wcpe.OreGifts.Utils.Actionbar;
import com.wcpe.OreGifts.Utils.RandomList;

import me.clip.placeholderapi.PlaceholderAPI;

public class List1_12 implements Listener {
	private final BlockFace[] faces = new BlockFace[] { BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH,
			BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };

	@SuppressWarnings("deprecation")
	@EventHandler
	public void FromTo(BlockFromToEvent e) {
		Block to = e.getToBlock();
		Block block = e.getBlock();
		int toid = to.getType().getId();
		int id = block.getType().getId();
		// id是否为岩浆和水
		if (id >= 8 && id <= 11) {
			// 判断两面是否岩浆 水 且对面不能为空气
			if (!(toid == 0) && spawnCobble(id, to)) {

				List<String> worlds = Main.LoadConfig().getConfig().getStringList("Worlds");
				// 判断世界
				if (worlds.contains(e.getBlock().getLocation().getWorld().getName())) {
					// 读取所有概率 统计
					List<Double> TotalChance = new ArrayList<>(Main.gifts.size());
					for (Gifts gift : Main.gifts) {
						double Chance = gift.getChance();
						if (Chance < 0)
							Chance = 0;
						TotalChance.add(Chance);
					}
					// 随机抽取一个id
					int IndexId = RandomList.Random(TotalChance);

					// 如果为true直接放置方块
					if (containsGift(IndexId, Main.gifts)) {
						Material material = getMaterial(IndexId);
						try {
							to.setType(material);
						}catch(java.lang.IllegalArgumentException ee) {
							
						}
					} else {
						// fasle 存储位置
						Material material = getMaterial(IndexId);
						int x = to.getX();
						int y = to.getY();
						int z = to.getZ();
						String world = to.getWorld().getName();
						Location loc = new Location(Bukkit.getWorld(world), x, y, z);
						String name = Main.gifts.get(IndexId).getName();
						List<String> lore = Main.gifts.get(IndexId).getLore();

						List<String> commands = Main.gifts.get(IndexId).getCommands();

						String giftmaterial = Main.gifts.get(IndexId).getGiftMaterial();
						String key = world + ";" + x + ";" + y + ";" + z;
						Main.Blockdata.put(key,
								new GiftsData(loc,IndexId, name, lore, commands, material.toString(), giftmaterial));
						// 然后接着放置
						try {
							to.setType(material);
						}catch(java.lang.IllegalArgumentException ee) {
							Bukkit.getConsoleSender().sendMessage("§4OreGifts配置文件Chances列表第" + IndexId + "个物品Material配置不是方块！");
						}
					}
				}
			}
		}
	}

	// 打破gifts掉落监听
	@EventHandler
	public void Break(BlockBreakEvent e) {
		Block block = e.getBlock();
		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();
		String world = e.getBlock().getWorld().getName();
		String key = world+";"+x+";"+y+";"+z;
		
		//如果是gift
		if(Main.Blockdata.containsKey(key)) {
			e.setDropItems(false);
			GiftsData giftsData = Main.Blockdata.get(key);
			List<String> commands = giftsData.getCommands();
			List<String> lore = giftsData.getLore();
			String name = giftsData.getName();
			Material giftMaterial = getGiftMaterial(giftsData);
			if(!giftMaterial.equals(Material.AIR)) {
				//创建掉落gifts
				ItemStack item = new ItemStack(giftMaterial);
				ItemMeta im = item.getItemMeta();
				
				//设置属性 name不为空设置
				if(name != null) {
					im.setDisplayName(name);
				}
				//设置属性 lore不为空设置
				if(lore!=null && !lore.isEmpty()){
					im.setLore(lore);
				}
				

				//加上属性
				item.setItemMeta(im);
				//丢下物品
				block.getWorld().dropItem(giftsData.getLocation(), item);
			}
			

			//执行指令			
			for (String st : commands) {
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
										pd[1] = PlaceholderAPI.setPlaceholders(e.getPlayer().getPlayer(),
												pd[1]);
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
			//移除列表中物品
			Main.Blockdata.remove(key);
			//在加强循环中删除 会抛错
			//所以return;
			return;
		}
	}

	// 检测material是否存在
	public Material getMaterial(int IndexId) {
		// 使用id读取物品material 并大写
		String materialname = Main.gifts.get(IndexId).getMaterial().toUpperCase();
		try {
			// 转换成material
			Material material = Material.valueOf(materialname);
			return material;
		} catch (java.lang.IllegalArgumentException ee) {
			Bukkit.getConsoleSender().sendMessage("§4OreGifts配置文件Chances列表第" + IndexId + "个物品Material配置错误！");
			return Material.AIR;
		}
	}
	
	// 检测GiftMaterial是否存在
	public Material getGiftMaterial(GiftsData giftdata) {
		// 使用id读取物品material 并大写
		String materialname = giftdata.getGiftMaterial().toUpperCase();
		try {
			// 转换成material
			Material material = Material.valueOf(materialname);
			return material;
		} catch (java.lang.IllegalArgumentException ee) {
			Bukkit.getConsoleSender().sendMessage("§4OreGifts配置文件Chances列表第" + giftdata.getIndex() + "个物品GiftMaterial配置错误！");
			return Material.AIR;
		}
	}
	
	//获取papi的方法
	public FileConfiguration PlaceholderAPIfile() {
		FileConfiguration config = YamlConfiguration
				.loadConfiguration(new File(Main.LoadConfig().getDataFolder(), "PlaceholderAPI.yml"));
		return config;
	}

	/*
	 * 判断是直接放置的方块还是需要检测的gifts true为直接放置 false为gifts方块
	 */
	public boolean containsGift(int IndexId, List<Gifts> list) {
		List<String> commands = list.get(IndexId).getCommands();
		List<String> lore = list.get(IndexId).getLore();
		String name = list.get(IndexId).getName();
		String giftMaterial = list.get(IndexId).getGiftMaterial();
		// commands为空
		if ((commands == null || commands.size() == 0) && (lore == null || lore.size() == 0) && (name == null)
				&& (giftMaterial == null)) {
			return true;
		} else {
			return false;
		}
	}

	// 判断水和岩浆方法
	@SuppressWarnings("deprecation")
	public boolean spawnCobble(int id, Block b) {
		int mirrorID1 = (id == 8 || id == 9) ? 10 : 8;
		int mirrorID2 = (id == 8 || id == 9) ? 11 : 9;
		for (BlockFace face : this.faces) {
			Block r = b.getRelative(face, 1);
			if (r.getType().getId() == mirrorID1 || r.getType().getId() == mirrorID2) {
				return true;
			}
		}
		return false;
	}

}