package com.wcpe.OreGifts;

import java.util.List;

public class Gifts {
	public Gifts(Integer index,String name,String material,List<String> lore,List<String> commands,Double chance,String giftmaterial) {
		this.index = index;
		this.name = name;
		this.material = material;
		this.lore = lore;
		this.commands = commands;
		this.chance = chance;
		this.giftmaterial = giftmaterial;
	}
	private Integer index;
	private String name;
	private List<String> lore;
	private List<String> commands;
	private String material;
	private Double chance;
	private String giftmaterial;
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
	public Double getChance() {
		return chance;
	}
	public String getGiftMaterial() {
		return giftmaterial;
	}
}
