package com.wcpe.OreGifts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;

import com.wcpe.BukkitUtils.CmdUtil;
import com.wcpe.BukkitUtils.ItemStackUtil;
import com.wcpe.OreGifts.Entity.Gifts;
import com.wcpe.OreGifts.Entity.Ore;

public class List1_12 implements Listener {
	public List1_12(Main a) {
		this.a = a;
	}

	private Main a;
	private final BlockFace[] faces = new BlockFace[] { BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH,
			BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };

	@SuppressWarnings("deprecation")
	@EventHandler
	public void FromTo(BlockFromToEvent e) {
		Block to = e.getToBlock();
		Block block = e.getBlock();
		int id = block.getType().getId();
		List<Ore> list = a.getOres().get(block.getLocation().getWorld().getName());
		if (list == null) {
			return;
		}
		if (id < 8 && id > 11) {
			return;
		}
		if ((to.getType().getId() == 0) && spawnCobble(id, to)) {
			Ore ore = a.Random(list);
			if (ore != null) {
				if (ore.getName() != null && ore.getLore() != null && ore.getGiftMaterial() != null) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("name", ore.getName());
					map.put("material", ore.getGiftMaterial());
					map.put("lore", ore.getLore());
					map.put("commands", ore.getCommands());
					map.put("amount", ore.getAmount());
					map.put("permission", ore.getPermission());
					Location loc = to.getLocation();
					a.getGifts().put(loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";"
							+ loc.getBlockZ(), new Gifts(map));
				}
				to.setType(Material.valueOf(ore.getMaterial().toUpperCase()));
			}
		}
	}

	@EventHandler
	public void Break(BlockBreakEvent e) {
		Location loc = e.getBlock().getLocation();
		Gifts gf = a.getGifts()
				.get(loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ());
		if (gf != null) {
			if(e.getPlayer().hasPermission(gf.getPermission())) {
				e.setDropItems(false);
				if (!Material.AIR.toString().equals(gf.getMaterial())) {
					e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), ItemStackUtil
							.getItem(gf.getMaterial().toUpperCase(), gf.getName(), gf.getLore(), gf.getAmount()));
				}
				CmdUtil.executionCommands(gf.getCommands(), a.isHasPapi(), e.getPlayer());
			}
			a.getGifts().remove(
					loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ());
		}
	}

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