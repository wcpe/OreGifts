package com.wcpe.OreGifts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.wcpe.OreGifts.Obj.Gifts;

public class Commands implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender player, Command cmd, String lab, String[] args) {
		if (cmd.getName().equalsIgnoreCase("oregifts")) {
			if (args.length == 1) {
				if (args[0].equals("reload")) {
					if (player instanceof Player) {
						if (player.hasPermission("oregifts.reload")) {
							reloadGifts();
							player.sendMessage("§b[OreGifts]§c开始重载配置文件!");
						} else {
							player.sendMessage("§b[OreGifts]§4c您没有权限使用这个指令！");
						}
					} else {
						reloadGifts();
						player.sendMessage("§b[OreGifts]§c开始重载配置文件!");

					}
				}
			} else {
				player.sendMessage("§b[OreGifts]§e/oregifts reload 重载配置文件!");
			}
		}
		return false;
	}

	void reloadGifts() {
		File file = new File(Main.LoadConfig().getDataFolder(),"config.yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		List<Gifts> list = new ArrayList<Gifts>();
		try {
			for (int t = 0; t >= 0; t++) {
				if (config.getString("Chances." + String.valueOf(t)) == null) {
					Main.gifts =list;
					break;
				}
				String Name = config.getString("Chances." + t + ".Name");
				String Material = config.getString("Chances." + t + ".Material");
				List<String> Lore = config.getStringList("Chances." + t + ".Lore");
				List<String> Commands = config.getStringList("Chances." + t + ".Commands");
				double chance = config.getDouble("Chances." + t + ".Chance");
				String giftmaterial = config.getString("Chances." + t + ".GiftMaterial");
				list.add(new Gifts(t, Name, Material, Lore, Commands, chance, giftmaterial));
			}
			
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
			Bukkit.getServer().getConsoleSender().sendMessage("§b[OreGifts]§4请检查配置文件中是否从0开始一直往后");
		}
	}
}
