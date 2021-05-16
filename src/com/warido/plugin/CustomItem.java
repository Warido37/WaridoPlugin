package com.warido.plugin;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItem {

    public static ItemStack customItem(ItemStack item, String displayName, List<String> lore){
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        item.setItemMeta(meta);
       
        return item;
    }
    @SuppressWarnings("deprecation")
	public static ItemStack customItem(Player player, ItemStack item, String displayName, List<String> lore){
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        item.setItemMeta(meta);
        player.updateInventory();
        return item;
    }
    public static boolean isCustomItem(ItemStack item){
        if(item.hasItemMeta()){
            if((item.getItemMeta().hasDisplayName()) ||
                    item.getItemMeta().hasLore()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    public static boolean isCustomItem(ItemStack item, String displayName){
        if(item.hasItemMeta()){
            if((item.getItemMeta().hasDisplayName())){
                if(item.getItemMeta().getDisplayName().equalsIgnoreCase(displayName)){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    public static boolean isCustomItem(ItemStack item, List<String> lore){
        if(item.hasItemMeta()){
            if((item.getItemMeta().hasLore())){
                if(item.getItemMeta().getLore().equals(lore)){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    public static boolean isCustomItem(ItemStack item, String displayName, List<String> lore){
        if(item.hasItemMeta()){
            if((item.getItemMeta().hasDisplayName()) &&
                    item.getItemMeta().hasLore()){
                if(item.getItemMeta().getDisplayName().equalsIgnoreCase(displayName) &&
                        item.getItemMeta().getLore().equals(lore)){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}