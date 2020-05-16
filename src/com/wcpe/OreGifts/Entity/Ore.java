package com.wcpe.OreGifts.Entity;

import java.util.List;

public class Ore {
	public Ore(String name, String material, String giftMaterial, List<String> lore, List<String> commands,
			double chance,int amount) {
		super();
		this.name = name;
		this.material = material;
		this.giftMaterial = giftMaterial;
		this.lore = lore;
		this.commands = commands;
		this.chance = chance;
		this.amount = amount;
	}
	private String name;
	private String material;
	private String giftMaterial;
	private List<String> lore;
	private List<String> commands;
	private double chance;
	private int amount;
	public String getName() {
		return name;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getGiftMaterial() {
		return giftMaterial;
	}
	public void setGiftMaterial(String giftMaterial) {
		this.giftMaterial = giftMaterial;
	}
	public List<String> getLore() {
		return lore;
	}
	public void setLore(List<String> lore) {
		this.lore = lore;
	}
	public List<String> getCommands() {
		return commands;
	}
	public void setCommands(List<String> commands) {
		this.commands = commands;
	}
	public double getChance() {
		return chance;
	}
	public void setChance(double chance) {
		this.chance = chance;
	}
	@Override
	public String toString() {
		return "Ore [name=" + name + ", material=" + material + ", giftMaterial=" + giftMaterial + ", lore=" + lore
				+ ", commands=" + commands + ", chance=" + chance + ", amount=" + amount + "]";
	}
	
}
