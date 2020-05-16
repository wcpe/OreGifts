package com.wcpe.BukkitUtils;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackUtil {
	/**
     * @param type 物品的种类
     * @param name 物品的名称
     * @param lore 物品的Lore
     * @return ItemStack
     */
    public static ItemStack getItem(String type,String name, List<String> lore) {
        return getItem(type,name,lore,1);
    }
    /**
     * @param type 物品的种类
     * @param name 物品的名称
     * @param lore 物品的Lore
     * @param amount 物品的数量
     * @return ItemStack
     */
    public static ItemStack getItem(String type,String name, List<String> lore,int amount) {
        ItemStack item = new ItemStack(Material.valueOf(type.toUpperCase()));
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lore);
        item.setItemMeta(im);
        item.setAmount(amount);
        return item;
    }
}
