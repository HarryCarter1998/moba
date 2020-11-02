package me.yoast.moba.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.yoast.moba.Main;
import me.yoast.moba.ui.InventoryUI;

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
		if(title.equals(InventoryUI.inventory_name)) {
			e.setCancelled(true);
			if(e.getCurrentItem() == null) {
				return;
			}
			e.getWhoClicked().closeInventory();
			InventoryUI.clicked((Player) e.getWhoClicked(), e.getSlot());
			
		}
	}
}
