package me.yoast.moba.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.yoast.moba.Main;
import me.yoast.moba.mobs.Creep;

public class CreepDropListener implements Listener{
	
	public CreepDropListener(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onMobDeath(EntityDeathEvent e) {
		e.getDrops().clear();
		e.setDroppedExp(0);
	}
}
