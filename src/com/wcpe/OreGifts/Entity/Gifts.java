package com.wcpe.OreGifts.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class Gifts implements ConfigurationSerializable {
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("material", material);
		map.put("lore", lore);
		map.put("commands", commands);
		map.put("amount", amount);
		return map;
	}
	@SuppressWarnings("unchecked")
	public Gifts(Map<String, Object> map) {
		super();
		this.name = (String) map.get("name");
		this.material = (String) map.get("material");
		this.lore = (List<String>) map.get("lore");
		this.commands = (List<String>) map.get("commands");
		this.amount = (int) map.get("amount");
	}

	private String name;
	private String material;
	private List<String> lore;
	private List<String> commands;
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
	@Override
	public String toString() {
		return "Gifts [name=" + name + ", material=" + material + ", lore=" + lore + ", commands=" + commands
				+ ", amount=" + amount + "]";
	}


}
