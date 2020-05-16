package com.wcpe.BukkitUtils;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WCPE
 */
public final class LocationUtil {
	
	/**
	 *@param needGo 点坐标
	 *@param AA 一个对角点
	 *@param BB 另一个对角点
	 *@apiNote 判断needGo是否在AA BB之间
	 *@return 返回boolean
	 */
	public static boolean isInAABB(Location needGo, Location AA, Location BB) {
		int xMax = (int) (Math.max(AA.getX(), BB.getX()));
		int xMin = (int) (Math.min(AA.getX(), BB.getX()));
		int yMax = (int) (Math.max(AA.getY(), BB.getY()));
		int yMin = (int) (Math.min(AA.getY(), BB.getY()));
		int zMax = (int) (Math.max(AA.getZ(), BB.getZ()));
		int zMin = (int) (Math.min(AA.getZ(), BB.getZ()));
		return (needGo.getX() >= xMin) && (needGo.getX() <= xMax) && (needGo.getY() >= yMin) && (needGo.getY() <= yMax)
				&& (needGo.getZ() >= zMin) && (needGo.getZ() <= zMax);
	}
	/**
	 *@param a 一个对角点
	 *@param b 另一个对角点
	 *@apiNote 获得之间所有坐标
	 *@return 返回List<Location>
	 */
	public static List<Location> getAllLocation(Location a, Location b) {
		int maxX = Math.max(a.getBlockX(), b.getBlockX());
		int maxy = Math.max(a.getBlockY(), b.getBlockY());
		int maxZ = Math.max(a.getBlockZ(), b.getBlockZ());
		int minX = Math.min(a.getBlockX(), b.getBlockX());
		int minY = Math.min(a.getBlockY(), b.getBlockY());
		int minZ = Math.min(a.getBlockZ(), b.getBlockZ());
		List<Location> list = new ArrayList<>();
		for (int t1 = minX; t1 <= maxX; t1++) {
			for (int t2 = minY; t2 <= maxy; t2++) {
				for (int t3 = minZ; t3 <= maxZ; t3++) {
					Location bb = new Location(a.getWorld(), t1, t2, t3);
					list.add(bb);
				}
			}
		}
		return list;
	}
	/**
	 *@param a 一个对角点
	 *@param b 另一个对角点
	 *@apiNote 获得之间所有方块
	 *@return 返回List<Block>
	 */
	public static List<Block> getAllBlock(Location a, Location b) {
		List<Block> c = new ArrayList<>();
		getAllLocation(a, b).forEach((s) -> {
			c.add(s.getBlock());
		});
		return c;
	}
}
