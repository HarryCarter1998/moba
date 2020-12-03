package me.yoast.moba.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftMagmaCube;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import me.yoast.moba.Main;
import me.yoast.moba.mobs.MobaPlayer;

public class EntityDeathListener implements Listener{
	
	private Main plugin;
	
	public EntityDeathListener(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent event) {
		Player killer = event.getEntity().getKiller();
		if(killer != null) {
			MobaPlayer mobaKiller = this.plugin.getMobaPlayer(killer);
			if(event.getEntity() instanceof CraftZombie) {
				mobaKiller.setGold(mobaKiller.getGold()+1);
				killer.sendMessage(ChatColor.GOLD + "+1 Gold");
			}
			if(event.getEntity() instanceof CraftMagmaCube) {
				mobaKiller.setGold(mobaKiller.getGold()+20);
				killer.sendMessage(ChatColor.GOLD + "+20 Gold");
			}
			
		}
	}
	

}
