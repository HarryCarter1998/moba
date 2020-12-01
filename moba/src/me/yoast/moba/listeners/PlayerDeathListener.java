package me.yoast.moba.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.yoast.moba.Main;

public class PlayerDeathListener implements Listener{
	
	
	public PlayerDeathListener(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		event.setKeepInventory(true);
		
	}
	

}