package com.wcpe.OreGifts;

import java.util.List;

import org.bukkit.Location;

public class GiftsData {
	public GiftsData(Location loc,Integer index,String name,List<String> lore,List<String> commands,String material,String giftmaterial) {
		this.loc = loc;
		this.index = index;
		this.name = name;
		this.material = material;
		this.lore = lore;
		this.commands = commands;
		this.giftmaterial = giftmaterial;
	}
	private Location loc;
	private Integer index;
	private String name;
	private List<String> lore;
	private List<String> commands;
	private String material;
	private String giftmaterial;
	public Location getLocation() {
		return loc;
	}
	public Integer getIndex() {
		return index;
	}
	public String getName() {
		return name;
	}
	public List<String> getLore() {
		return lore;
	}
	public List<String> getCommands() {
		return commands;
	}
	public String getMaterial() {
		return material;
	}
	public String getGiftMaterial() {
		return giftmaterial;
	}
}
