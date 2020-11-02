package me.yoast.moba.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import me.yoast.moba.Main;

public class DropItemListener implements Listener{
	
	public DropItemListener(Main plugin) {
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
		
	}

	@EventHandler
	public void onThrow(PlayerDropItemEvent e) {
			//e.setCancelled(true);
			
	}
}

