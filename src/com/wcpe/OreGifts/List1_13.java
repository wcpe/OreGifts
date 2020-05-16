package com.wcpe.OreGifts;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
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

public class List1_13 implements Listener {
	public List1_13(Main a) {
		this.a = a;
	}

	private Main a;

	@EventHandler
	public void FromTo(BlockFromToEvent e) {
		Block to = e.getToBlock();
		if (to.getType().equals(Material.AIR) && spawnCobble(e)) {
			if (!a.getWorlds().contains(e.getBlock().getLocation().getWorld().getName())) {
				return;
			}
			Ore ore = a.Random(a.getOres());
			if (ore != null) {
				e.setCancelled(true);
				if (ore.getName() != null && ore.getLore() != null && ore.getGiftMaterial() != null) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("name", ore.getName());
					map.put("material", ore.getGiftMaterial());
					map.put("lore", ore.getLore());
					map.put("commands", ore.getCommands());
					map.put("amount", ore.getAmount());
					Location loc = to.getLocation();
					a.getGifts().put(loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";"
							+ loc.getBlockZ(), new Gifts(map));
				}
				Bukkit.getScheduler().runTask(a, () -> {
					to.setType(Material.valueOf(ore.getMaterial().toUpperCase()));
				});
			}
		}
	}

	@EventHandler
	public void Break(BlockBreakEvent e) {
		Location loc = e.getBlock().getLocation();
		Gifts gf = a.getGifts()
				.get(loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ());
		if (gf != null) {
			e.setDropItems(false);
			if (!Material.AIR.toString().equals(gf.getMaterial())) {
				e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), ItemStackUtil
						.getItem(gf.getMaterial().toUpperCase(), gf.getName(), gf.getLore(), gf.getAmount()));
			}
			CmdUtil.executionCommands(gf.getCommands(), a.isHasPapi(), e.getPlayer());
			a.getGifts().remove(loc.getWorld().getName()+";"+loc.getBlockX()+";"+loc.getBlockY()+";"+loc.getBlockZ());
		}
	}

	public boolean spawnCobble(BlockFromToEvent event) {
		Type fromType = this.getType(event.getBlock());
		if (fromType != null && event.getFace() != BlockFace.DOWN) {
			Block b = event.getToBlock();
			Type toType = this.getType(event.getToBlock());
			Location fromLoc = b.getLocation();
			if (fromType == Type.LAVA || fromType == Type.LAVA_STAT) {
				if (!isSurroundedByWater(fromLoc)) {
					return false;
				}
			}
			if ((toType != null || b.getType() == Material.AIR) && (generatesCobble(fromType, b))) {

				return true;
			}
		}
		return false;
	}

///////////////////////////////////////////////////////////////////////////////////////
	// 引用开源代码CustomOreGen
///////////////////////////////////////////////////////////////////////////////////////
	private BlockFace[] blockFaces = { BlockFace.NORTH, BlockFace.WEST, BlockFace.EAST, BlockFace.SOUTH };

	public boolean isSurroundedByWater(Location fromLoc) {

		for (BlockFace blockFace : blockFaces) {
			if (this.getType(fromLoc.getBlock().getRelative(blockFace)) == Type.WATER
					|| this.getType(fromLoc.getBlock().getRelative(blockFace)) == Type.WATER_STAT) {
				return true;
			}
		}

		return false;

	}

	private enum Type {
		WATER, WATER_STAT, LAVA, LAVA_STAT
	}

	public Type getType(Block b) {
		try {
			Class.forName("org.bukkit.block.data.Levelled");
			if (b.getBlockData() != null && b.getBlockData() instanceof org.bukkit.block.data.Levelled) {
				org.bukkit.block.data.Levelled level = (org.bukkit.block.data.Levelled) b.getBlockData();
				if (level.getLevel() == 0) {
					if (level.getMaterial() == Material.WATER) {
						return Type.WATER_STAT;
					} else {
						return Type.LAVA_STAT;
					}
				} else {
					if (level.getMaterial() == Material.WATER) {
						return Type.WATER;
					} else {
						return Type.LAVA;
					}
				}
			}
		} catch (ClassNotFoundException e) {
			switch (b.getType().name()) {
			case "WATER":
				return Type.WATER;
			case "STATIONARY_WATER":
				return Type.WATER_STAT;
			case "LAVA":
				return Type.LAVA;
			case "STATIONARY_LAVA":
				return Type.LAVA_STAT;
			}
		}
		return null;
	}

	private final BlockFace[] faces = { BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST,
			BlockFace.SOUTH, BlockFace.WEST };

	public boolean generatesCobble(Type type, Block b) {
		Type mirrorType1 = (type == Type.WATER_STAT) || (type == Type.WATER) ? Type.LAVA_STAT : Type.WATER_STAT;
		Type mirrorType2 = (type == Type.WATER_STAT) || (type == Type.WATER) ? Type.LAVA : Type.WATER;
		for (BlockFace face : this.faces) {
			Block r = b.getRelative(face, 1);
			if ((this.getType(r) == mirrorType1) || (this.getType(r) == mirrorType2)) {
				return true;
			}
		}
		return false;
	}

	public static enum Material113 {
		STATIONARY_LAVA(10), STATIONARY_WATER(9), WATER(8), LAVA(11), AIR(0);

		int id;

		Material113(int id) {
			this.id = id;
		}

		int getID() {
			return this.id;
		}
	}

}
