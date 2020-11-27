package me.yoast.moba.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.yoast.moba.Main;

public class FoodLevelChangeListener implements Listener{
	
	
	public FoodLevelChangeListener(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		
	}
	
	@EventHandler
	public void onDeath(FoodLevelChangeEvent event) {
		event.setCancelled(true);
		
	}
	

}
