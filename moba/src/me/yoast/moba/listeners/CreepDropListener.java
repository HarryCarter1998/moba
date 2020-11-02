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
import me.yoast.moba.ui.InventoryUI;

public class CreepDropListener implements Listener{
	
	private Main plugin;
	
	public CreepDropListener(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onMobDeath(EntityDeathEvent e) {
		// TODO Make players give more gold than creeps (if e.getEntity instanceof Player, addItem GOLD_NUGGET x10 instead)
		if (e.getEntity().getKiller() instanceof Player) {
			e.getEntity().getKiller().getInventory().addItem(new ItemStack(Material.GOLD_NUGGET, 1));
		}
		if (e.getEntity() instanceof CraftZombie) {
			e.getDrops().clear();
		}
	}
}
