package me.yoast.moba.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SlimeSplitEvent;

import me.yoast.moba.Main;

public class SlimeSplitListener implements Listener{
	
	private Main plugin;
	
	public SlimeSplitListener(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onSplit(SlimeSplitEvent e) {
		e.setCancelled(true);
	}
}
