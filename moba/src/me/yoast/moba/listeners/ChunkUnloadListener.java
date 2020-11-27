package me.yoast.moba.listeners;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftMagmaCube;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import me.yoast.moba.Main;

public class ChunkUnloadListener implements Listener{
	
	private Main plugin;
	
	public ChunkUnloadListener(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

//	@EventHandler(priority = EventPriority.LOWEST)
//	public void onEntityDespawn(ChunkUnloadEvent e) {
//	    for(Entity entity:e.getChunk().getEntities()) {
//	        if(entity instanceof CraftMagmaCube || entity instanceof CraftZombie) { 
//	        	e.setCancelled(true);
//	        	break;
//	        }
//	    }
//	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDespawn(EntityDeathEvent e) {
		if(e.getEntity() instanceof CraftMagmaCube) {
			Bukkit.broadcastMessage("Magma has died");
			Bukkit.broadcastMessage(e.getEntity().getLastDamageCause().getCause().toString());
	}
	}
}
