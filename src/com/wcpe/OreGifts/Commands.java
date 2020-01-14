package com.wcpe.OreGifts;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender player, Command cmd, String lab, String[] args) {
		if (cmd.getName().equalsIgnoreCase("oregifts")) {
			if (args.length == 1) {
				if (args[0].equals("reload")) {
					if (player instanceof Player) {
						if (player.hasPermission("oregifts.reload")) {
							Main.LoadConfig().reloadConfig();
							player.sendMessage("§b[OreGifts]§c开始重载配置文件!");
						}else {
							player.sendMessage("§b[OreGifts]§4c您没有权限使用这个指令！");
						}
					} else {
						Main.LoadConfig().reloadConfig();
						player.sendMessage("§b[OreGifts]§c开始重载配置文件!");
					}
				}
			}else {
				player.sendMessage("§b[OreGifts]§e/oregifts reload 重载配置文件!");
			}
		}
		return false;
	}
}
