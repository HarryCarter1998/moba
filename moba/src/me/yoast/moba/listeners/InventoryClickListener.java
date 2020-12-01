package me.yoast.moba.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.yoast.moba.Main;
import me.yoast.moba.utils.Utils;
import me.yoast.moba.xmlreader.XMLReader;

public class InventoryClickListener implements Listener{
	
	private Main plugin;
	
	public InventoryClickListener(Main plugin) {
		this.plugin = plugin;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
		
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		//e.setCancelled(true);
		String title = e.getInventory().getTitle();
		if(title.equals(Utils.chat("&f&lClasses"))) {
			e.setCancelled(true);
			if(e.getCurrentItem() == null) {
				return;
			}
			e.getWhoClicked().closeInventory();
			setClassItems((Player) e.getWhoClicked(), e.getSlot(), this.plugin);
		}
	}
	public void setClassItems(Player player, int slot, Main plugin) {
		List<String> xmlItems = XMLReader.getItems(slot);
		List<ItemStack> items = new ArrayList<ItemStack>();
		List<ItemStack> armor = new ArrayList<ItemStack>();
		for(int i=0; i<xmlItems.size(); i++) {
			String itemString = xmlItems.get(i);
			ItemStack item = new ItemStack(Material.getMaterial(itemString), 1);
			if(isArmor(item)) {
				armor.add(item);
			} else {
				items.add(item);
			}
		}
		this.plugin.getMobaPlayer(player).setClassItems(items);
		Bukkit.broadcastMessage(items.toString());
		this.plugin.getMobaPlayer(player).setClassArmor(armor);
	}
	
	public boolean isArmor(ItemStack itemStack) {
        if (itemStack == null)
            return false;
        final String typeNameString = itemStack.getType().name();
        if (typeNameString.endsWith("_HELMET")
                || typeNameString.endsWith("_CHESTPLATE")
                || typeNameString.endsWith("_LEGGINGS")
                || typeNameString.endsWith("_BOOTS")) {
            return true;
            }

        return false;
    }
}
