package me.yoast.moba.listeners;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftMagmaCube;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import me.yoast.moba.Main;
import me.yoast.moba.mobs.MobaPlayer;
import me.yoast.moba.mobs.Nexus;
import me.yoast.moba.ui.Shop;

	
	public class PlayerInteractEntityListener implements Listener{
		
		
		private Main plugin;
		
		public PlayerInteractEntityListener(Main plugin) {
			Bukkit.getPluginManager().registerEvents(this, plugin);
			this.plugin = plugin;
		}
		
		@EventHandler
		public void onClick(PlayerInteractEntityEvent event) {
			Player player = event.getPlayer();
			MobaPlayer mobaPlayer = this.plugin.getMobaPlayer(player);
			Entity clicked = event.getRightClicked();
			if(clicked instanceof CraftMagmaCube) {
				CraftMagmaCube clickedMagma = (CraftMagmaCube) clicked;
				if(clickedMagma.getSize() == 3) {
					Nexus clickedNexus = (Nexus) clickedMagma.getHandle();
					if(clickedNexus.getTeam().toString() == mobaPlayer.getTeam().toString()) {
						new Shop(player, this.plugin);
					}
				}
				
			}
		}

}
